import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import App from './App'
import router from './router'
import firebase from 'firebase'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.config.productionTip = false;
Vue.use(BootstrapVue);

let app;
let config = {
    apiKey: "AIzaSyDkNjyt6dMrpVtHJ3z_P6knDH302sXRHEY",
    authDomain: "my-awesome-project-a819f.firebaseapp.com",
    databaseURL: "https://my-awesome-project-a819f.firebaseio.com",
    projectId: "my-awesome-project-a819f",
    storageBucket: "my-awesome-project-a819f.appspot.com",
    messagingSenderId: "863302148257"
};
firebase.initializeApp(config);

firebase.auth().onAuthStateChanged(user => {
    /* eslint-disable no-new */
    app = new Vue({
        el: '#app',
        router,
        render: h => h(App)
    });
});

