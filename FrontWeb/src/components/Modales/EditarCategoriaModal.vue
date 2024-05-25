<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import FormularioAgregarCategoria from '../Formularios/FormularioEditarCategoria.vue';


import axios from 'axios';

export default {
    name: 'editarCategoriaModal',
    components: {
        Boton,
        Contenedor,
        FormularioAgregarCategoria
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            editado: false,
            categoria: {}
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            if (valor) {
                this.dialog = true
                this.editado = false
            }else{
                this.dialog = false
            }
        },
        setearCategoria(categoria) {
            this.categoria = categoria
        }
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
            <template v-if="!editado">
                <Contenedor style="width: 562px;">
                    <template v-slot:titulo>
                        Editar categoria
                    </template>
                    <template v-slot:contenido>
                        <FormularioAgregarCategoria :categoria="categoria" @cerrar-dialogo="toggleDialog(false)" @categoriaCreada="editado = true; $emit('refetch')"/>
                    </template>
                </Contenedor>
            </template>
            <template v-else>
                <Contenedor style="padding-bottom: 34px;">
                    <template v-slot:titulo>
                        
                    </template>
                    <template v-slot:contenido>
                        La categoria fue actualizada exitosamente
                        <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                    </template>
                </Contenedor>
            </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>