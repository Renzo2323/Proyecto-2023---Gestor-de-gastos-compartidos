<script>
import Boton from '../Botones/Boton.vue'
import axios from 'axios';

export default {
    name: 'deshabilitarCategoriaDialogo',
    components: {
        Boton
    },
    data() {
        return {
            dialog: false,
            categoria: {},
            errorPostSubmit: "",
            terminado: false,
            estadoInicial: '',
            estadoFinal: ''
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            if (valor) {
                this.terminado = false
            }
            this.dialog = valor
        },
        setearCategoria(categoria) {
            this.categoria = categoria
            if (categoria.estado) {
                this.estadoInicial = 'habilitada'
                this.estadoFinal = 'deshabilitada'
            } else {
                this.estadoInicial = 'deshabilitada'
                this.estadoFinal = 'habilitada'
            }
        },
        async deshabilitarCategoria() {//Esta funcion puede habilitar y deshabilitar
            let url
            if (this.estadoInicial == 'deshabilitada') {
                url = import.meta.env.VITE_API + "/api/categorias/adm/activar/" + this.categoria.id
            } else {
                url = import.meta.env.VITE_API + "/api/categorias/adm/desactivar/" + this.categoria.id
            }

            const response = await axios.post(url, {},
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.terminado = true
                        this.$emit('refetch')
                    }
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        }
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent>
        <v-card style="padding: 10px; padding-top: 20px; padding-bottom: 20px;">
            <template v-if="!terminado">
                <v-card-text style="padding-bottom: 20px;">
                    Seguro/a que desea <span v-if="estadoInicial == 'deshabilitada'">habilitar</span><span
                        v-else>deshabilitar</span> la categoria <span style="color: var(--color-primario);">{{
                            categoria.nombre }}</span>?
                    <p id="errorControl">{{ errorPostSubmit }}</p>
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton style="margin-right: 10px;" tipo="primario2" @click="deshabilitarCategoria()"><span
                            v-if="estadoInicial == 'deshabilitada'">Habilitar</span><span v-else>Deshabilitar</span></Boton>
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cancelar</Boton>
                </v-card-actions>
            </template>
            <template v-else>
                <v-card-text style="padding-bottom: 20px;">
                    La categoria fue <span v-if="estadoInicial == 'deshabilitada'">habilitada</span><span
                        v-else>deshabilitada</span> exitosamente
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                </v-card-actions>
            </template>

        </v-card>
    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>