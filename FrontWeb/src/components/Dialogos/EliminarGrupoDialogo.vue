<script>

import Boton from '../Botones/Boton.vue';
import axios from 'axios';

export default {
    name: 'eliminarGrupo',
    components: {
        Boton
    },
    data() {
        return {
            dialog: false,
            grupoId: NaN,
            errorPostSubmit: "",
            eliminado: false
        }
    },
    methods: {
        toggleDialog(valor) {
            if(valor){
                this.eliminado = false
            }
            this.dialog = valor
        },
        setearGrupoId(grupoId) {
            
            this.grupoId = grupoId
        },
        async eliminarGrupo() {            
            const data = this.$route.params.token

            const url = import.meta.env.VITE_API + "/api/grupos/usf/eliminarGrupoDeGastos/" + this.grupoId
            const response2 = await axios.delete(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.eliminado = true
                        this.$emit('refetch')
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

        },
        volverMenu() {
            
            window.location.href = '/';
        }
    },
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent>
        <v-card style="padding: 10px; padding-top: 20px; padding-bottom: 20px;">
            <template v-if="!eliminado">
                <v-card-text style="padding-bottom: 20px;">
                    Esta seguro de eliminar el grupo?
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton style="margin-right: 10px;" tipo="primario2" @click="eliminarGrupo()">Eliminar</Boton>
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cancelar</Boton>
                </v-card-actions>
            </template>
            <template v-else>
                <v-card-text style="padding-bottom: 20px;">
                    El grupo ha sido eliminado.
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton tipo="secundarioBorde" @click="volverMenu()">Cerrar</Boton>
                </v-card-actions>
            </template>

        </v-card>
    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>