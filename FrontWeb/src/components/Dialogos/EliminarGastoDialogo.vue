<script>

import Boton from '../Botones/Boton.vue';
import axios from 'axios';

export default {
    name: 'eliminarGastoDialogo',
    components: {
        Boton
    },
    data() {
        return {
            dialog: false,
            gastoId: NaN,
            errorPostSubmit: "",
            eliminado: false,
            nombreGasto: ''
        }
    },
    methods: {
        toggleDialog(valor) {
            if(valor){
                this.eliminado = false
            }
            this.dialog = valor
        },
        setDatos(gastoId, gastoNombre) {
            this.gastoId = gastoId
            this.nombreGasto = gastoNombre
        },
        async eliminarGasto() {            
            const url = import.meta.env.VITE_API + "/api/gastosCompartidos/usf/deleteGastoCompartido/" + this.gastoId
            const response = await axios.delete(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.eliminado = true
                        this.$emit('refetch')
                    }


                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

        },
    },
    emits: ['refetch'],
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent>
        <v-card style="padding: 10px; padding-top: 20px; padding-bottom: 20px;">
            <template v-if="!eliminado">
                <v-card-text style="padding-bottom: 20px;">
                    Seguro/a que desea eliminar el gasto <span style="color: var(--color-primario);">{{ nombreGasto }}</span>?
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton style="margin-right: 10px;" tipo="primario2" @click="eliminarGasto()">Eliminar</Boton>
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cancelar</Boton>
                </v-card-actions>
            </template>
            <template v-else>
                <v-card-text style="padding-bottom: 20px;">
                    El gasto ha sido eliminado.
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                </v-card-actions>
            </template>

        </v-card>
    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>