<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import FormularioEditarGrupo from '../Formularios/FormularioEditarGrupo.vue';


import axios from 'axios';

export default {
    name: 'editarGrupoModal',
    components: {
        Boton,
        Contenedor,
        FormularioEditarGrupo
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
    },
    props: {
        infoGrupo : Object
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
            <template v-if="!editado">
                <Contenedor style="width: 562px;">
                    <template v-slot:titulo>
                        Editar grupo
                    </template>
                    <template v-slot:contenido>
                        <FormularioEditarGrupo ref="a" :infoGrupo="infoGrupo" @cerrar-dialogo="toggleDialog(false)" @grupoModificado="editado = true; $emit('refetch')"/>
                    </template>
                </Contenedor>
            </template>
            <template v-else>
                <Contenedor style="padding-bottom: 34px;">
                    <template v-slot:titulo>
                        
                    </template>
                    <template v-slot:contenido>
                        El grupo fue actualizado exitosamente.
                        <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                    </template>
                </Contenedor>
            </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>