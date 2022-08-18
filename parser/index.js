const express = require('express');
const port = 8585;
const bodyParser = require('body-parser');
const playwright = require("playwright");
const app = express();
const requestM = require('request')
const fs = require('fs')
let err = []
let cookieWithCaptcha = []

const getTextByCaptchaImg = (imgUrl) => {
    return new Promise(function (resolve, reject) {

        requestM(imgUrl, null, (err, res, body) => {

            const formData = {
                // Pass a simple key-value pair
                key: 'ace6f1d17044db697ba7ba01e5fb6534',
                // Pass data via Streams
                file: fs.createReadStream(__dirname + '/captcha.png'),
            };
            requestM.post({
                url: 'http://rucaptcha.com/in.php',
                formData: formData
            }, async function optionalCallback(err, httpResponse, body) {
                if (err) {
                    return console.error('upload failed:', err);
                }
                console.log('Upload successful!  Server responded with:', body);
                const id = body.replace(/^\D+/g, '');
                console.log("task id ", id)

                let timerId = setInterval(() => {

                    requestM('http://rucaptcha.com/res.php?key=ace6f1d17044db697ba7ba01e5fb6534&action=get&id=' + id,
                        null, async (err, res, body) => {

                            console.log('Опрос результата капчи....')
                            const text = body.replace(/^OK\|/g, '')

                            if (text !== 'CAPCHA_NOT_READY') {
                                clearInterval(timerId)
                                console.log("Текст капчи ", text)
                                resolve(text)
                            } else {
                                console.log("Капча еще не решена!!!!")
                            }

                        });

                }, 1000);

            });

        })
            .pipe(fs.createWriteStream('captcha.png'))

    });
}

const median = (values) => {
    if (values.length === 0) throw new Error("No inputs");

    values.sort(function (a, b) {
        return a - b;
    });

    var half = Math.floor(values.length / 2);

    if (values.length % 2)
        return values[half];

    return (values[half - 1] + values[half]) / 2.0;
}

// Use Node.js body parsing middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false,
}));


app.get('/', async (request, response) => {

    const url = request.query.q
    const deep = request.query.deep || 1
    let title = null

    console.log('url: ' + url, 'deep: ' + deep)

    if (!url) {
        response.send({
                result: null,
                error: "url not found"
            }
        );
        return;
    }

    let allPricesGlobal = []

    const browser = await playwright.chromium.launch({
        headless: true // set this to true
    });


    async function mostActive(cnt) {
        const page = await browser.newPage();
        page.setDefaultTimeout(120000)

        if (cnt === 0) {
            console.log("парсинг закончен, закрываем страницу!")
            await page.close()
            return
        }

        if (cnt > 10) {
            console.log("OOPS! max url loop count is 10")
            err.push("OOPS! max url loop count is 10")
            await page.close()
            return
        }
        let cntBlock = null

        if (cookieWithCaptcha.length > 0) {

            await page.context().clearCookies()
            console.log("куки почищены!!!")
            await page.context().addCookies(cookieWithCaptcha)
            console.log("новые куки установлены!!!")


        } else {
            console.log("пока нет новых кук!!!")
        }

        console.log("начинаем парсинг " + cnt + " страницы!!!", new Date())
        const isQ = /[?]/m.test(url)

        const fullUrl = isQ ? url + "&page=" + cnt : url + "?page=" + cnt

        await page.goto(fullUrl);

        const needCaptcha = await page.locator('//*[@id="root"]/div/div/form/div[2]/div/div/div[1]/input').count()
        console.log("title: " + await page.title())
        if (needCaptcha === 0) {
            title = await page.title()
            const hasContent = await page.locator('.ListingCars_outputType_list').count()
            if (hasContent === 0) {
                console.log("OOPS! no content to parse, url: " + fullUrl)
                await page.close()
                await mostActive(--cnt)
                return
            }
            console.log("Страница спаршена успешно без капчи!!!")
            await getContent(page, cntBlock, cnt)

        } else {
            console.log("Капча!!! нажимаем я не робот")
            await page.click('.CheckboxCaptcha-Button')
            await page.waitForTimeout(2000);
            const ifTextCaptcha = await page.locator('//*[@id="advanced-captcha-form"]/div/div[1]/img').count();
            if (ifTextCaptcha === 0) {
                console.log("captcha текстом не нужна!!!")
                await page.close()
                await mostActive(cnt)
            } else {
                console.log("нужна некстовая капча!!!")
                const imgCaptcha = await page.$eval('//*[@id="advanced-captcha-form"]/div/div[1]/img', img => {
                    return img.getAttribute('src')
                })
                console.log("url captcha ", imgCaptcha)

                await getTextByCaptchaImg(imgCaptcha).then(async val => {
                    if (val === 'ERROR_CAPTCHA_UNSOLVABLE') {
                        console.log("не удалось распознать капчу!!!")
                        await page.close()
                        await mostActive(cnt)
                    }
                    await page.locator('#xuniq-0-1').fill(val);
                    await page.click('#advanced-captcha-form > div > div.AdvancedCaptcha-FormActions > button.Button2.Button2_size_l.Button2_view_action')
                    await page.waitForTimeout(2000);
                    console.log(await page.title())

                    const ifCaptchaTrue = await page.locator('.ListingCars_outputType_list').count();
                    if (ifCaptchaTrue === 0) {
                        console.log("Неправильная капча!!!")
                        await page.close()
                        await mostActive(cnt)
                    }
                    console.log("Капча успешно прошла!!!!!!")

                    cookieWithCaptcha = await page.context().cookies()
                    console.log("Куки успешно получены!")

                    await getContent(page, cntBlock, cnt)

                })
            }
        }

        async function getContent(page, cntBlock, cnt) {
            cntBlock = await page.$eval('.ListingCars_outputType_list',
                divElm => {
                    let refs = []
                    let atags = divElm.querySelectorAll('.ListingItemPrice__content span');
                    for (let item of atags) {
                        refs.push(Number(item.textContent.replace(/\D/g, '')));
                    }
                    return refs;
                });

            allPricesGlobal.push(...cntBlock)
            await page.close()
            await mostActive(--cnt)
        }

    }

    mostActive(deep).then(async (res) => {

        if (err.length > 0) {
            response.send({
                    result: null,
                    error: err.toString()
                }
            );
            return
        }

        if (allPricesGlobal.length === 0) {
            response.send({
                    result: null,
                    error: "Oops! parser cant parse..."
                }
            );
            return
        }

        const result = median(allPricesGlobal)
        const avg = Math.round(allPricesGlobal.reduce((a, b) => a + b, 0) / allPricesGlobal.length) || 0;
        const min = Math.min.apply(Math, allPricesGlobal)
        const max = Math.max.apply(Math, allPricesGlobal)
        let r = {
            result: {
                title: title,
                all: allPricesGlobal,
                cnt: allPricesGlobal.length,
                median: result,
                avg: avg,
                min: min,
                max: max,
            },
            error: null
        }
        console.log("SUCCESS!!! есть результат: " + JSON.stringify(r))

        await browser.close()
        response.send(r);
    })

});

// Start the server
const server = app.listen(port, async (error) => {
    if (error) return console.log(`Error: ${error}`);

    console.log(`Server listening on port ${server.address().port}`);
});