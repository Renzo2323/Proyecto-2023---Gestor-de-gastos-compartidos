<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import FormularioAgregarCategoria from '../Formularios/FormularioAgregarCategoria.vue';


import axios from 'axios';

export default {
    name: 'agregarCategoriaModal',
    components: {
        Boton,
        Contenedor,
        FormularioAgregarCategoria
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
                this.dialog = true
                this.creado = false
            }else{
                this.dialog = false
            }
        }
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
            <template v-if="!creado">
                <Contenedor style="width: 562px;">
                    <template v-slot:titulo>
                        Agregar categoria
                    </template>
                    <template v-slot:contenido>
                        <FormularioAgregarCategoria @cerrar-dialogo="toggleDialog(false)" @categoriaCreada="creado = true; $emit('refetch')"/>
                    </template>
                </Contenedor>
            </template>
            <template v-else>
                <Contenedor style="padding-bottom: 34px;">
                    <template v-slot:titulo>
                        
                    </template>
                    <template v-slot:contenido>
                        La categoria fue creada exitosamente
                        <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                    </template>
                </Contenedor>
            </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>