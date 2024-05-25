<script>

import LeftBarBase from './LeftBarBase.vue';
import Boton from '../Botones/Boton.vue';
import { RouterLink } from 'vue-router'

import axios from 'axios';

export default {
    name: 'leftBarUsuario',
    components: {
        LeftBarBase,
        Boton
    },
    data() {
        return {
            opcionesBasicas: [
                { texto: 'Mis Grupos', icono: 'mdi-account-group', ruta: '/Grupos', dropdown: true },
                // { texto: 'Estadísticas personales', icono: 'mdi-poll', ruta: '/EstadisticasPersonales', dropdown: false },
                // { texto: 'Ajustes', icono: 'mdi-cog', ruta: '/Ajustes', dropdown: false },
            ],
            grupos: [],
            mostrarBarra: true,
            val: false,
            
        }
    },
    methods: {
        tipoGrupoAIcono(tipo) {
            switch (tipo) {
                case "viaje": return 'mdi-airplane'
                case "casa": return 'mdi-home'
                case "evento": return 'mdi-calendar'
                case "pareja": return 'mdi-heart'
                case "proyecto": return 'mdi-clipboard-outline'
                case "otro": return 'mdi-account-group'
                default: return 'mdi-account-group'
            }
        },
        getRutaGrupo(id) {
            return "/grupo/" + id
        },
        toggleLeftBar(valor) {
            
            this.mostrarBarra = valor
        }
    },
    async mounted() {
        const url = import.meta.env.VITE_API + "/api/grupos/usf/obtenerMisGrupos"
        this.grupos = []
        const response = await axios.get(url,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                }
            })
            .then(function (response) {
                response.data.forEach(grupo => {
                    this.grupos.push({
                        id: grupo.id,
                        nombre: grupo.nombre,
                        tipo: grupo.tipoGrupo.toLowerCase(),
                    })
                });


            }.bind(this))
            .catch(function (error) {
                this.errorPostSubmit = "No se pudo realizar esta accion"
            }.bind(this));
    }
}
</script>

<template>
    <v-hover v-slot="{ isHovering, props }">
        <LeftBarBase v-bind="props" v-if="mostrarBarra">
            <div v-for="(opcion, i) in opcionesBasicas" :key="i">
                <RouterLink v-if="!opcion.dropdown" :to="opcion.ruta" style="text-decoration: none; color: white;">
                    <v-list-item :value="opcion.ruta">
                        <template v-slot:prepend>
                            <v-icon :icon="opcion.icono" style="color: white; opacity: 1!important;"></v-icon>
                        </template>
                        <v-list-item-title v-text="opcion.texto" class="textoVuetify"></v-list-item-title>
                    </v-list-item>
                </RouterLink>
                <v-list-group v-if="opcion.dropdown" :value="isHovering">
                    <template v-slot:activator="{ props }">
                        <v-list-item :value="opcion.ruta" v-bind="props">
                            <template v-slot:prepend>
                                <v-icon :icon="opcion.icono" style="color: white; opacity: 1!important;"></v-icon>
                            </template>
                            <v-list-item-title v-text="opcion.texto" class="textoVuetify"></v-list-item-title>
                        </v-list-item>
                    </template>
                    <RouterLink to="/crearGrupo">
                        <Boton icono="mdi-plus" tipo="secundario"
                            style="margin-left: 60px; width: 65%; margin-top: 5px; margin-bottom: 5px;">Nuevo grupo</Boton>
                    </RouterLink>

                    <template v-for="(grupo, j) in grupos" :key="j">
                        <RouterLink :to="getRutaGrupo(grupo.id)" style="text-decoration: none; color: white;">
                            <v-list-item :value="grupo.id">
                                <template v-slot:prepend>
                                    <v-icon :icon="tipoGrupoAIcono(grupo.tipo)"
                                        style="color: white; opacity: 1!important;"></v-icon>
                                </template>
                                <v-list-item-title v-text="grupo.nombre" class="textoVuetify"></v-list-item-title>
                            </v-list-item>
                        </RouterLink>

                    </template>

                </v-list-group>

            </div>

            




        </LeftBarBase>
    </v-hover>
</template>

<style scoped>
.textoVuetify {
    font-size: 14px;
}
</style>