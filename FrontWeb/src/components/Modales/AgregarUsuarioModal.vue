<script>
import Boton from '../Botones/Boton.vue'
import FormularioAgregarUsuario from '../Formularios/FormularioAgregarUsuario.vue';
import Contenedor from '../Contenedores/Contenedor.vue'

import axios from 'axios';

export default {
    name: 'agregarUsuarioModal',
    components: {
        Boton,
        FormularioAgregarUsuario,
        Contenedor
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
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
            <template v-if="!creado">
                <Contenedor style="width: 562px;">
                    <template v-slot:titulo>
                        Agregar usuario
                    </template>
                    <template v-slot:contenido>
                        <FormularioAgregarUsuario @cerrar-dialogo="toggleDialog(false)" @usuario-agregado="creado = true; $emit('refetch')"/>
                    </template>
                </Contenedor>
            </template>
            <template v-else>
                <Contenedor style="padding-bottom: 34px;">
                    <template v-slot:titulo>
                        
                    </template>
                    <template v-slot:contenido>
                        El usuario fue creado exitosamente
                        <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                    </template>
                </Contenedor>
            </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>