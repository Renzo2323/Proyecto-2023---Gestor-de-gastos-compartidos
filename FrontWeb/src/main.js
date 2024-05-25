import './assets/css/main.css'


import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'vuetify/styles'
import { createVuetify } from 'vuetify/lib/framework.mjs'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { aliases, mdi } from 'vuetify/iconsets/mdi'
import { VDataTable } from 'vuetify/lib/labs/components.mjs'
import axios from 'axios'


const vuetify = createVuetify({
    components:{
        ...components,
        VDataTable
    },
    directives,
    icons: {
        defaultSet: 'mdi',
        aliases,
        sets: {
            mdi
        }
    },
    theme: {
        themes: {
            splitIt: {
                dark: false,
                colors: {
                    rojoSplitIt: '#FF3D60',
                },
            },
        },
    }
})


const app = createApp(App)

app.use(router)
app.use(vuetify)


app.mount('#app')
