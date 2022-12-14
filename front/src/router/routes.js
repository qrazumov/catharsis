const routes = [
  {
    name: 'index',
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {name: 'postsPage', path: '', component: () => import('pages/PostsPage')},
      {name: 'postPage', path: '/post/:id', component: () => import('pages/PostPage.vue')},
      {name: 'categoryPage', path: 'category', component: () => import('pages/CategoryPage')},
      {name: 'postsByCategoryPage', path: '/category/:id', component: () => import('pages/PostsByCategoryPage')},
      {name: 'wMIPage', path: '/whoami', component: () => import('pages/WMIPage.vue')},
      {name: 'mapPage', path: '/map', component: () => import('pages/MapPage.vue')},

      {name: 'parserPage', path: '/project/parser', component: () => import('pages/ParserPage')},
    ]
  },
  {
    name: 'office',
    path: '/office',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {name: 'notePage', path: 'note', component: () => import('pages/office/NotePage')},
      {name: 'notePageID', path: 'note/:id', component: () => import('pages/office/NotePageID')},
      {name: 'loginPage', path: 'login', component: () => import('pages/office/LoginPage')},
      {name: 'registerPage', path: 'register', component: () => import('pages/office/RegisterPage')},
      {name: 'lkPage', path: 'id', component: () => import('pages/office/LKPage')},
      {name: 'logoutPage', path: 'logout', component: () => import('pages/office/LogoutPage')},
    ]
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
