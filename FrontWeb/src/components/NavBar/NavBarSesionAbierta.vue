<script>
import NavBarBase from './NavBarBase.vue';
import Boton from '../Botones/Boton.vue';
import { RouterLink } from 'vue-router'
import Notificaciones from '../Notificaciones.vue';
import axios from 'axios';

export default {
    name: 'navBarSesionAbierta',
    components: {
        NavBarBase,
        Boton,
        Notificaciones
    },
    data() {
        return {
            opciones: [
                { title: 'Mi Perfil', ruta: '/perfil' },
                { title: 'Cambiar contraseña', ruta: '/cambioContrasena' },
                { title: 'Cerrar Sesión', ruta: '/cerrarSesion' },
            ],
            imagenPerfil: '/src/assets/svg/User.svg',
            mostrarBarra: true,
            mostrarBadge: false,
            mostrarNotificaciones: false,
            dialog: false
        }
    },
    methods: {
        async fetchDatos() {
            const url = import.meta.env.VITE_API + "/api/usuarios/usr/infoUsuario"

            const response = await axios.get(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        if (response.data.imagen && response.data.imagen != 'null') {
                            this.imagenPerfil = response.data.imagen
                        } else {
                            this.imagenPerfil = '/src/assets/svg/User.svg'
                        }
                    }
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

        },
        toggleNavBar(valor) {
            
            this.mostrarBarra = valor
        },
        toggleBadge(valor){
            this.mostrarBadge = valor
        }
    },
    mounted() {
        this.fetchDatos()
    },
    watch: {
        $route(to, from) {
            this.fetchDatos()
        }
    },
}
</script>


<template>
    <NavBarBase v-if="mostrarBarra">
        <template v-slot:primero>
            <v-row justify="center">
                <v-dialog v-model="dialog" scrollable width="auto">
                    <template v-slot:activator="{ props }">
                        

                        <!-- <v-badge dot color="white" offset-x=28 offset-y=14 > -->

                            <div id="wrapper" style="margin-right: 15px;">
                                <img @click="toggleBadge(false); mostrarNotificaciones = false; mostrarNotificaciones = true" src="../../assets/svg/Bell.svg"  v-bind="props"
                            class="botonNavBar" />
                                <div v-if="mostrarBadge" id="overlay" style="background-color: white; border-radius: 50%; width: 15px; height: 15px; border-style: solid; border-width: 3px; border-color: var(--color-primario);">
                                </div>
                            </div>
                            


                        <!-- </v-badge> -->
                    </template>
                    <Notificaciones @cerrar="dialog = false" v-if="mostrarNotificaciones" />
                </v-dialog>
            </v-row>
        </template>
        <template v-slot:segundo>
            <v-menu>
                <template v-slot:activator="{ props }">
                    <img v-if="imagenPerfil == '/src/assets/svg/User.svg'" src="/src/assets/svg/User.svg"
                        class="botonNavBar" v-bind="props" style="width: 60px;margin-top: 5px;" />
                    <img :src="`data:image/png;base64,${imagenPerfil}`" class="roundedd botonNavBar "
                        v-if="imagenPerfil != '/src/assets/svg/User.svg'" v-bind="props">
                </template>
                <v-list>
                    <RouterLink v-for="(item, index) in opciones" :key="index" :to="item.ruta"
                        style="text-decoration: none; color: black;">
                        <v-list-item :value="index">
                            <v-list-item-title class="textoVuetify">{{ item.title }}</v-list-item-title>
                        </v-list-item>
                    </RouterLink>

                </v-list>
            </v-menu>
        </template>
    </NavBarBase>
</template>

<style scoped>
.registroLink {
    color: white;
    text-decoration: none;
}

.textoVuetify {
    font-size: 14px;
}

.botonPerfil {
    display: inline;
    margin: 0 auto;
    margin-left: -25%;
    height: 100%;
    width: auto;
}

.imagenWrapper {
    width: 35px;
    height: 35px;
    position: relative;
    overflow: hidden;
    border-radius: 50%;
    border-width: 3px;
    border-color: white;
    border-style: solid;
}

.botonNavBar {
    cursor: pointer;
}

.subtitulo {
    font-size: 10px;
}

img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 35px;
    width: 35px;
    border-width: 2px;
    border-color: rgb(255, 255, 255);
    border-style: solid;
    margin-top: 5px;

}

#overlay{
    position: absolute;
    
    bottom: 8px;
    right: 11px;
    pointer-events: none;
    animation: badgeAnimation 1s linear infinite;
}

#wrapper{
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
}

@keyframes badgeAnimation {
    0%, 100%{
        width: 15px;
        height: 15px;
        right: 9px;
        bottom: 8px;
    }
    50%{
        width:11px;
        height: 11px;
        right: 11px;
        bottom: 10px;
    }
}
</style>