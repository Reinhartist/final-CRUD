import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import login from '@/components/login'
import firebase from 'firebase'

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/tables',
      name: 'HelloWorld',
      component: HelloWorld,
      meta: {
          requiresAuth: true
      }
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '*',
      redirect: '/login'
    },
  ]
});

export default router;

router.beforeEach((to, from, next) => {
    let currentUser = firebase.auth().currentUser;
    let requiresAuth = to.matched.some(record => record.meta.requiresAuth);

    if(requiresAuth && !currentUser) next('login');
    else if (!requiresAuth && currentUser) next('tables');
    else next();
});
