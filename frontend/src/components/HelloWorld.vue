<template>
  <div class="service">

    <ul class="nav" id="navbar">
      <li v-for="op in menuops" v-bind:class="{active: msg === op}"
          @click="handleChange(op)">{{op}}</li>
        <li @click="logout">Logout</li>
    </ul>

      <b-input-group class="row justify-content-md-center">
          <b-input-group-append >

              <b-form-input class="col-10" v-bind:placeholder="'Search for ' + msg.toLowerCase()"
                            title="" v-model="searchfield"></b-form-input>

              <b-button @click="findThings()">Search</b-button>
              <b-button @click="searchfield = ''; findThings()">Find All</b-button>
              <b-button @click="create = !create; editConfirm = -1;
               resetForm()">Create</b-button>

          </b-input-group-append>
      </b-input-group><br>

        <div class="container">
          <div class="row align-items-center justify-content-center" v-if="create || editConfirm !== -1">
              <b-form @submit="create ? createItem() : updateItem()">

                      <b-form-input v-for="field in formPlaceHolders[msg]" :key="field.key" required
                                     v-model="form[msg][field['key']]" v-bind:placeholder="
                                     `Enter ${field.label}`">
                      </b-form-input>

                      <b-form-checkbox v-if="msg === 'Projects'" v-model="form.Projects.active">
                          Project is active</b-form-checkbox>

                  <b-button type="submit" variant="primary">Submit</b-button>
                  <b-button variant="danger" @click="allocateResource"
                            v-if="msg === 'Resources' &&
                            editConfirm !== -1 && response[editConfirm].owner !== null">
                      Deallocate</b-button>
              </b-form>
              <div v-if="msg === 'Resources' && editConfirm !== -1
              && response[editConfirm].owner === null">
                  <b-form-input pattern="[1-9][0-9]*" placeholder="Assign to account"
                                v-model="resourceOwner">
                  </b-form-input>
                  <b-button variant="primary" @click="allocateResource">Allocate</b-button>
              </div>

              <div v-if="msg === 'Projects' && editConfirm !== -1">
                  <b-form-input pattern="[1-9][0-9]*" placeholder="Worker account" v-model="resourceOwner">
                  </b-form-input>
                  <b-button variant="primary" @click="changeWorkers(true)">Assign</b-button>
                  <b-button variant="danger" @click="changeWorkers(false)">
                      Remove</b-button>
              </div>
          </div>
        </div>

    <b-table striped hover dark table-bordered :items="response"
             :fields="result_fields[msg]" :per-page="perPage" :current-page="currentPage">
        <template slot="actions" slot-scope="row">

            <b-button v-if="deleteConfirm !== row.index" variant="primary"
                      @click="handleEdit(row.index)">
                {{editConfirm === row.index ? "Cancel" : "Edit"}}
            </b-button>
            <b-button v-if="editConfirm !== row.index" variant="danger"
                    @click="deleteConfirm = (deleteConfirm === row.index) ? -1 : row.index">
                {{deleteConfirm === row.index ? "Cancel" : "Delete"}}
            </b-button>
            <b-button v-if="deleteConfirm === row.index" variant="success"
                      @click="deleteById(row.item, row.index); deleteConfirm = -1">Confirm</b-button>

        </template>
    </b-table><br>

      <b-pagination align="center" size="md" :total-rows="response.length"
                    v-model="currentPage" :per-page="perPage"></b-pagination>
  </div>
</template>

<script>
    import axios from 'axios'
    import firebase from 'firebase'

    export default {
        name: 'service',
        data () {
            return {
                msg: 'People',
                create: false,
                response: [],
                errors: [],
                menuops: ["Resources", "People", "Projects"],
                searchfield: '',
                currentPage: 1,
                perPage: 7,
                editConfirm: -1,
                deleteConfirm: - 1,
                resourceOwner: '',
                projectWorker: '',
                map: {"Resources": "resource", "People": "person", "Projects": "project"},
                result_fields: {
                    "Resources":
                        [
                            "id",
                            {key: "name", sortable: true},
                            {key: "fullName", label: "Owner Name",
                                formatter: (value, key, item) => {
                                    if (!item.owner) return;
                                    return `${item.owner.firstName} ${item.owner.lastName}`
                                }
                            },
                            "owner.accountNumber",
                            "actions"
                        ],

                    "People":
                        [
                            {key: "firstName", sortable: true},
                            {key: "lastName", sortable: true},
                            {key: "accountNumber", sortable: true},
                            {key: "resources", label: "Resources",
                                    formatter: resourcesArray =>
                                        resourcesArray.map(resource => resource.name).join(", ")},
                            {key: "projects", label: "Projects",
                                    formatter: projectsArray =>
                                        projectsArray.map(project => project.name).join(", ")},
                            "actions"
                        ],
                    "Projects": [
                        "id",
                        {key: "name", sortable: true},
                        {
                            key: "workers", label: "Workers",
                            formatter: workersArray => {
                                if(workersArray === null) return "";
                                return workersArray.map(worker =>
                                    `${worker.firstName} ${worker.lastName} - ${worker.accountNumber}`)
                                    .join(", ");
                            }
                        },
                        {
                            key: "resources", label: "Resources",
                            formatter: (value, key, item) => {
                                if(item.workers === null) return "";
                                return item.workers.map(worker => worker.resources.map(resource =>
                                    resource.name).join(", ")).join(", ")
                            }
                        },
                        {key: "active", sortable: true},
                        "actions"
                    ]
                },
                formPlaceHolders: {
                    Resources: [{key: "name", label: "resource name"}],
                    People: [
                        {key: "firstName", label: "first name"},
                        {key: "lastName", label: "last name"}
                    ],
                    Projects: [{key: "name", label: "project name"}]
                },
                form: {
                    Resources: {name: ''},
                    People: {firstName: '', lastName: ''},
                    Projects: {name: '', active: false}
                }
            }
        },
        methods: {
            findThings() {
                const search = this.searchfield;
                axios.get(`/api/${this.map[this.msg]}/findBy?name=${search}`)
                    .then(response => this.response = response.data)
                    .catch(e => {});
            },
            deleteById(item, index) {
                axios.delete(`/api/${this.map[this.msg]}/delete?id=${item.id}`)
                    .then(response => this.response.splice(index, 1))
                    .catch(e => {});
            },
            createItem() {
                const formData = new FormData();
                Object.keys(this.form[this.msg]).forEach(key => formData.append(key,
                    this.form[this.msg][key]));
                this.create = false;
                axios({
                    method: 'post',
                    url: `/api/${this.map[this.msg]}/save${this.map[this.msg]}`,
                    data: formData,
                }
                ).then(response => this.response.unshift(response.data))
                 .catch(e => {});
            },
            updateItem() {
                const formData = new FormData();
                Object.keys(this.form[this.msg]).forEach(key => formData.append(key,
                    this.form[this.msg][key]));
                formData.append('id', this.response[this.editConfirm].id);
                axios({
                        method: 'post',
                        url: `/api/${this.map[this.msg]}/update${this.map[this.msg]}`,
                        data: formData,
                    }
                ).then(response => {
                    Object.keys(this.response[this.editConfirm]).forEach(k =>
                            this.response[this.editConfirm][k] = response.data[k]);
                    this.editConfirm = -1;
                }).catch(e => this.editConfirm = -1);
            },
            allocateResource() {
                const formData = new FormData();
                formData.append("resId", this.response[this.editConfirm].id);
                if(this.resourceOwner !== '') formData.append("accNo", this.resourceOwner);
                else formData.append("accNo", '-1');
                axios({
                    method: 'post',
                    url: '/api/resource/assignresource',
                    data: formData,
                }).then(response => {
                    if(this.resourceOwner === '') {
                        this.response[this.editConfirm].owner = null;
                    } else {
                        this.response[this.editConfirm].owner = {
                            "id": response.data.owner.id,
                            "lastName": response.data.owner.lastName,
                            "accountNumber": response.data.owner.accountNumber,
                            "firstName": response.data.owner.firstName
                        };
                    }
                    this.resourceOwner = '';
                }).catch(e => this.resourceOwner = '')

            },
            changeWorkers(change) {
                const formData = new FormData();
                formData.append("id", this.response[this.editConfirm].id);
                formData.append("accNo", this.resourceOwner);
                formData.append("assign", change);
                axios({
                    method: 'post',
                    url: '/api/project/changeworker',
                    data: formData,
                }).then(response => {
                    this.response[this.editConfirm].workers = response.data.workers;
                    this.response[this.editConfirm].resource = response.data;
                    this.resourceOwner = '';
                }).catch(e => this.resourceOwner = '');
            },
            handleChange(op) {
                if(this.msg !== op) {
                    this.response = [];
                    this.searchfield = "";
                    this.create = false;
                    this.deleteConfirm = -1;
                    this.editConfirm = -1;
                    this.msg = op;
                }
            },
            resetForm() {
                const form = this.form;
                form.Resources.name = '';
                form.People.firstName = '';
                form.People.lastName = '';
                form.Projects.active = false;
                form.Projects.name = '';
            },
            setDetails(row) {
                Object.keys(this.form[this.msg]).forEach(k =>
                    this.form[this.msg][k] = this.response[row][k]);
            },
            handleEdit(row) {
                this.editConfirm = (this.editConfirm === row) ? -1 : row;
                this.create = false;
                this.setDetails(row)
            },
            logout() {
                firebase.auth().signOut().then(() => this.$router.replace('login'));
            }
        }
    }
</script>

<style scoped>
    .nav {
        position: absolute;
        display: inline-block;
        top: 0;
        left: 0;
        width: 100%;
        color: rgb(53, 73, 94);
        background-color: rgb(65, 184, 131);
    }
    .nav li {
        display: inline-block;
        margin: 0 10px;
        font-weight: bold;
        width: 20%;
        text-align: center;
        vertical-align: middle;
        padding: 10px;
    }
    .active {
        background-color: chartreuse;
    }
    .nav li:hover {
        background-color: greenyellow;
    }
    h1, h2 {
        font-weight: normal;
    }
    ul {
        list-style-type: none;
        padding: 0;
    }
</style>