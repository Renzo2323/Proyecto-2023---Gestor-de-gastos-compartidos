<script>

import LeftBarBase from './LeftBarBase.vue';

export default {
    name: 'leftBarAdministrador',
    components: {
        LeftBarBase
    },
    data() {
        return {
            opcionesBasicas: [
                { texto: 'Gestión de usuarios', icono: 'mdi-account', ruta: '/GestionUsuarios' },
                { texto: 'Gestión de grupos', icono: 'mdi-account-multiple', ruta: '/GestionGrupos' },
                { texto: 'Gestión de categorías', icono: 'mdi-layers-triple', ruta: '/GestionCategorias' },
                // { texto: 'Estadísticas globales', icono: 'mdi-poll', ruta: '/EstadisticasGlobales' },
                { texto: 'Ajustes', icono: 'mdi-cog', ruta: '/AjustesAdministrador' },
            ],
            mostrarBarra: true
        }
    },
    methods: {
        toggleLeftBar(valor){
            
            this.mostrarBarra = valor
        }
    }
}
</script>

<template>
    <LeftBarBase v-if="mostrarBarra">
        <div v-for="(opcion, i) in opcionesBasicas" :key="i">
            <RouterLink :to="opcion.ruta" style="text-decoration: none; color: white;">
                <v-list-item :value="opcion.ruta">
                    <template v-slot:prepend>
                        <v-avatar>
                            <v-icon :icon="opcion.icono" style="color: white;"></v-icon>
                        </v-avatar>
                    </template>
                    <v-list-item-title v-text="opcion.texto" class="textoVuetify"></v-list-item-title>
                </v-list-item>
            </RouterLink>

        </div>
    </LeftBarBase>
</template>

<style scoped>
.textoVuetify {
    font-size: 14px;
}
</style>