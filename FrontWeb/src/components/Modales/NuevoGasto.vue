<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import FormularioNuevoGasto from '../Formularios/FormularioNuevoGasto.vue';
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue';

import axios from 'axios';

export default {
    name: 'nuevoGasto',
    components: {
        Boton,
        FormularioNuevoGasto,
        Contenedor,
        ContenedorCerrable
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            creado: false
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            if (valor) {
                this.creado = false
            }

            this.dialog = valor
        }
    },
    props:{
        integrantes : Array,
        categoriasGrupo: Array,
        grupoId : Number,
        monedaGrupo: String
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
            <template v-if="!creado">
                <ContenedorCerrable style="width: 562px;" @cerrar="toggleDialog(false)">
                    <template v-slot:titulo>
                        Nuevo gasto
                    </template>
                    <template v-slot:contenido>
                        <FormularioNuevoGasto :integrantes="integrantes" :categoriasGrupo="categoriasGrupo" :grupoId="grupoId" :monedaGrupo="monedaGrupo" @cerrar-dialogo="toggleDialog(false)" @gasto-agregado="creado = true; $emit('refetch')"/>
                    </template>
                </ContenedorCerrable>
            </template>
            <template v-else>
                <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="toggleDialog(false)">
                    <template v-slot:titulo>
                        
                    </template>
                    <template v-slot:contenido>
                        El gasto fue creado exitosamente
                        <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                    </template>
                </ContenedorCerrable>
            </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>

