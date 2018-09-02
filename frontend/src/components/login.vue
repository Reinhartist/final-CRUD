<template>
    <div class="container">
        <div class="row align-items-center justify-content-center">
            <b-form class="col-10">
            <b-form-input type="email" v-model="email" placeholder="email"></b-form-input>
            <b-form-input type="password" v-model="password" placeholder="password"></b-form-input>
            <b-button @click="login">Login</b-button>
            </b-form>
        </div>
    </div>
</template>

<script>
    import firebase from 'firebase'

    export default {
        name: "login",
        data () {
            return {
                email: '',
                password: ''
            }
        },
        methods: {
            login() {
                firebase.auth().signInWithEmailAndPassword(this.email, this.password).then(
                    user => this.$router.replace('tables'),
                    e => alert(e)
                )
            }
        }
    }
</script>

<style scoped>

</style>