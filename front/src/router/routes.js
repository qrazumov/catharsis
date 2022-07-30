const routes = [
  {
    name: 'index',
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {name: 'postsPage', path: '', component: () => import('pages/PostsPage')},
      {name: 'postPage', path: '/post/:id', component: () => import('pages/PostPage.vue')},
      {name: 'categoryPage', path: '/category', component: () => import('pages/CategoryPage')},
      {name: 'postsByCategoryPage', path: '/category/:id', component: () => import('pages/PostsByCategoryPage')},
      {name: 'wMIPage', path: '/whoami', component: () => import('pages/WMIPage.vue')},
      {name: 'mapPage', path: '/map', component: () => import('pages/MapPage.vue')}
    ]
  },
  {
    name: 'office',
    path: '/office',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {name: 'notePage', path: 'note', component: () => import('pages/office/NotePage')},
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
