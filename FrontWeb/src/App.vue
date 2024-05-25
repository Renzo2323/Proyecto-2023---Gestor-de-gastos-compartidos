<script>
import { RouterLink, RouterView } from 'vue-router'
import ContenedorTotal from './components/Contenedores/ContenedorTotal.vue';
import NavBarSesionCerrada from './components/NavBar/NavBarSesionCerrada.vue';
import NavBarSesionAbierta from './components/NavBar/NavBarSesionAbierta.vue';
import LeftBarAdministrador from './components/LeftBar/LeftBarAdministrador.vue';
import LeftBarUsuario from './components/LeftBar/LeftBarUsuario.vue';
import sockjs from "sockjs-client/dist/sockjs"
import { Stomp, Client } from "@stomp/stompjs"
import Boton from './components/Botones/Boton.vue';
import axios from 'axios';

export default {
  name: 'app',
  components: {
    ContenedorTotal,
    NavBarSesionCerrada,
    NavBarSesionAbierta,
    LeftBarAdministrador,
    LeftBarUsuario,
    Boton
  },
  data() {
    return {
      tipoUsuario: 'invitado', //Cambia la barra de navegacion
      terminoCargar: false,
      stompClient: null,
      messages: [],
      subscrito: "",
      subscripcion: null,
      notificacionVisual: false
    }
  },
  mounted() {
    if (localStorage.tipoUsuario) {
      this.tipoUsuario = localStorage.tipoUsuario
    } else {
      localStorage.setItem("tipoUsuario", "invitado")
    }
    
    this.conectar()

  },
  watch: {
    $route(to, from) {
      if (localStorage.tipoUsuario) {
        this.tipoUsuario = localStorage.tipoUsuario
      } else {
        this.tipoUsuario = "invitado"
      }
      this.terminoCargar = false
    }
  },
  computed: {
    mostrarLeftBar() {
      const { mostrarLeftBar } = this.$route.meta;

      return mostrarLeftBar || typeof mostrarLeftBar === 'undefined';
    }
  },
  methods: {
    toggleLeftBar(valor) {
      this.$refs.leftBar.toggleLeftBar(valor)
    },
    toggleNavBar(valor) {
      this.$refs.navBar.toggleNavBar(valor)
    },
    toggleBadge(valor) {
      if (typeof (this.$refs.navBar.toggleBadge) === 'function') {
        this.$refs.navBar.toggleBadge(valor)
      } else {
        setTimeout(function() { this.toggleBadge(valor); }.bind(this), 1000);
      }
      
      
    },
    connect() {
      const dom = import.meta.env.VITE_API
      const domSinProtocolo = dom.split('//')[1]
      
      const uurl = "ws://" + domSinProtocolo + "/notificaciones"
      this.stompClient = new Client({
        brokerURL: uurl,
        reconnectDelay: 1000,
        debug: function (str) {
        },
        connectHeaders: {
          login: 'user',
          passcode: 'password'
        }
      })

      let that = this
      var headers = {}
      this.stompClient.onConnect = (frame) => {
        that.handleMessageReceipt("Connected")
        this.stompClient.subscribe('/topic/broadcast', (message) => {
          that.handleMessageReceipt(message.body)
          that.notificacionVisual = true
        })
        this.subscribe()
      }
      this.stompClient.onWebSocketError = (error) => {
      };

      this.stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      };

      this.stompClient.activate();

    },
    async subscribe() {

      let that = this
      const url = import.meta.env.VITE_API + "/api/usuarios/usr/infoUsuario"
      const response = await axios.get(url,
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          }
        })
        .then(function (response) {
          if (response.data.estatus != null && response.data.estatus == false) {//Error
          } else {
            const id = response.data.id
            const topic = "/topic/" + id
            this.subscrito = "Subscrito a topic:" + topic
            this.subscripcion = this.stompClient.subscribe(topic, (message) => {
              that.handleMessageReceipt(message.body)
              that.notificacionVisual = true


              const sonido = new Audio('/sound/sonidoNotificacion.mp3')
              sonido.play()
              that.toggleBadge(true)
              that.refetchGrupo()
            }, { id: "testSub" })
          }

        }.bind(this))
        .catch(function (error) {
          this.errorPostSubmit = "No se pudo realizar esta accion"
        }.bind(this));

    },
    async fetchId() {

    },
    disconnect() {
      if (this.subscripcion != null) {
        this.subscripcion.unsubscribe("testSub")
      }
      this.subscrito = ""
      if (this.stompClient != null) {
        this.stompClient.deactivate();
      }
    },
    test() {
      if (this.stompClient != null) {
        this.stompClient.publish({
          destination: "/app/test",
          body: {
            mensaje: "Hola, servidor!"
          },
          headers: {
            UsuarioId: 7
          }
        })
      } else {
      }
    },
    test2() {
      if (this.stompClient != null) {
        this.stompClient.publish({
          destination: "/app/teset",
          body: {
            mensaje: "MMMMMMM!"
          },
          headers: {
            UsuarioId: 7
          }
        })
      } else {
      }
    },
    handleMessageReceipt(messageOutput) {
      this.messages.push(messageOutput)
    },
    refetchGrupo() {
      if (typeof (this.$refs.routerView.refetchWebSocket) === 'function') {
        this.$refs.routerView.refetchWebSocket()
      } else {
      }
    },
    async checkNotificaciones() {
      const response = await axios.get(import.meta.env.VITE_API + "/api/usuarios/usr/notificacionesUsuario",
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          }
        })
        .then(function (response) {
          if (response.data.estatus != null && response.data.estatus == false) {//Error
            this.errorPostSubmit = response.data.mensaje
          } else {
            this.notificaciones = response.data.reverse()
            response.data.every( notificacion =>{
              if(!notificacion.leida){
                this.toggleBadge(true)
                return false
              }
              return true
            })
          }

        }.bind(this))
        .catch(function (error) {
          
          this.errorPostSubmit = "No se pudo realizar esta accion"
        }.bind(this));
    },
    conectar(){
      this.disconnect()
      this.connect()
    }
  },
  beforeMount() {

  }
}

</script>

<template>
  <v-layout>
    <NavBarSesionCerrada ref="navBar" v-if="tipoUsuario == 'invitado'" />
    <NavBarSesionAbierta ref="navBar" v-if="tipoUsuario == 'usuario' || tipoUsuario == 'administrador'" />
    <template v-if="mostrarLeftBar">
      <LeftBarAdministrador ref="leftBar" v-if="tipoUsuario == 'administrador'" />
      <LeftBarUsuario ref="leftBar" v-if="tipoUsuario == 'usuario'" />
    </template>
    <v-main>
      <div v-show="terminoCargar">
        <RouterView v-slot="{ Component }" @conectar="conectar" @cerrarSesion="disconnect" @checkNotificaciones="conectar();checkNotificaciones()" @terminoCargar="terminoCargar = true"
          @toggleLeftBar="toggleLeftBar" @toggleNavBar="toggleNavBar" :key="$route.path">
          <component ref="routerView" :is="Component" />
        </RouterView>

        <v-snackbar v-model="notificacionVisual" location="top right" style="margin-top: 55px;" color="white">
          <p style="font-size: 14px;">{{ messages[messages.length - 1] }}</p>
          <template v-slot:actions>
            <v-btn icon="mdi-window-close" color="pink" variant="plain" @click="notificacionVisual = false">
            </v-btn>

          </template>
        </v-snackbar>


      </div>
      <div v-if="!terminoCargar" style="display:flex; height: 550px; align-items: center;">
        <div class="spinner">
          <div class="rect1"></div>
          <div class="rect2"></div>
          <div class="rect3"></div>
          <div class="rect4"></div>
          <div class="rect5"></div>
        </div>
      </div>
    </v-main>


  </v-layout>
</template>

<style scoped></style>
