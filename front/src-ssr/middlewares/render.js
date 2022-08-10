import { ssrMiddleware } from 'quasar/wrappers'

// This middleware should execute as last one
// since it captures everything and tries to
// render the page with Vue

export default ssrMiddleware(({ app, resolve, render, serve }) => {
  // we capture any other Express route and hand it
  // over to Vue and Vue Router to render our page
  app.get(resolve.urlPath('*'), (req, res) => {
    res.setHeader('Content-Type', 'text/html')

    render(/* the ssrContext: */ { req, res })
      .then(html => {
        // now let's send the rendered html to the client
        res.send(html)
      })
      .catch(err => {
        // oops, we had an error while rendering the page

        // we were told to redirect to another URL
        if (err.url) {
          if (err.code) {
            res.redirect(err.code, err.url)
          } else {
            res.redirect(err.url)
          }
        } else if (err.code === 404) {
          // hmm, Vue Router could not find the requested route

          // Should reach here only if no "catch-all" route
          // is defined in /src/routes
          let _404 = '<!DOCTYPE html><html lang="en-US" dir="ltr"><head><base href="/"><meta charset="utf-8"><title>404 not found</title><meta content="telephone=no" name="format-detection"><meta content="no" name="msapplication-tap-highlight"><meta content="user-scalable=no,initial-scale=1,maximum-scale=1,minimum-scale=1,width=device-width" name="viewport"><link href="favicon.ico" rel="icon" type="image/x-icon"><script type="text/javascript">!function(e,t,a,n,c,m,r){e.ym=e.ym||function(){(e.ym.a=e.ym.a||[]).push(arguments)},e.ym.l=1*new Date,m=t.createElement(a),r=t.getElementsByTagName(a)[0],m.async=1,m.src="https://mc.yandex.ru/metrika/tag.js",r.parentNode.insertBefore(m,r)}(window,document,"script"),ym(89847387,"init",{clickmap:!0,trackLinks:!0,accurateTrackBounce:!0,webvisor:!0})</script><noscript><div><img alt="" src="https://mc.yandex.ru/watch/89847387" style="position:absolute;left:-9999px"></div></noscript><link rel="stylesheet" href="/css/vendor.css"><link rel="stylesheet" href="/css/app.css"></head><body class="desktop no-touch body--light" data-server-rendered><script id="qmeta-init">window.__Q_META__={title:"",titleTemplate:null,meta:{},link:{},htmlAttr:{},bodyAttr:{}}</script><div id="q-app"><div class="fullscreen bg-blue text-white text-center q-pa-md flex flex-center"><div><div style="font-size:30vh">404</div><div class="text-h2" style="opacity:.4">Oops. Nothing here...</div><a class="q-btn q-btn-item non-selectable no-outline q-btn--unelevated q-btn--rectangle bg-white text-blue q-btn--actionable q-focusable q-hoverable q-btn--no-uppercase q-mt-xl" style="" tabindex="0" href="/"><span class="q-focus-helper"></span><span class="q-btn__content text-center col items-center q-anchor--skip justify-center row"><span class="block">Go Home</span></span></a></div></div></div><script>window.__INITIAL_STATE__={};var currentScript=document.currentScript;currentScript.parentNode.removeChild(currentScript)</script><script src="/js/vendor.js" defer="defer"></script><script src="/js/src_pages_ErrorNotFound_vue.js" defer="defer"></script><script src="/js/app.js" defer="defer"></script></body></html>'
          res.status(404).send(_404)
        } else if (process.env.DEV) {
          // well, we treat any other code as error;
          // if we're in dev mode, then we can use Quasar CLI
          // to display a nice error page that contains the stack
          // and other useful information

          // serve.error is available on dev only
          serve.error({ err, req, res })
        } else {
          // we're in production, so we should have another method
          // to display something to the client when we encounter an error
          // (for security reasons, it's not ok to display the same wealth
          // of information as we do in development)

          // Render Error Page on production or
          // create a route (/src/routes) for an error page and redirect to it
          res.status(500).send('500 | Internal Server Error')
        }
      })
  })
})
