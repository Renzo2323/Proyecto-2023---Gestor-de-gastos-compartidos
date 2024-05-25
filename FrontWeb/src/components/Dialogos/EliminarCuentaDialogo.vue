<script>
import Boton from '../Botones/Boton.vue'
import axios from 'axios';

export default {
    name: 'eliminarCuenta',
    components: {
        Boton
    },
    data() {
        return {
            dialog: false,
            usuario: {},
            errorPostSubmit: "",
            eliminado: false,
            contador: 10
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            this.contador = 10
            if(valor){
                this.eliminado = false
            }
            this.dialog = valor
        },
        setearUsuario(usuario) {
            this.usuario = usuario
        },
        async eliminarUsuario() {
            this.contador = this.contador -1
            if(this.contador > 0){
                return
            }

            const url = import.meta.env.VITE_API + "/api/usuarios/usr/borrarMiUsuario"
            const response = await axios.delete(url, 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        localStorage.clear()
                        localStorage.setItem("tipoUsuario", "invitado")
                        localStorage.setItem("token", "")
                        this.$router.push('/')
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
            <template v-if="!eliminado">
                <v-card-text style="padding-bottom: 20px;">
                    <p>Seguro/a que desea eliminar su cuenta?</p>
                    <p style="color: red">Esta acción no es revertible.</p>
                    <p id="errorControl">{{ errorPostSubmit }}</p>
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton style="margin-right: 10px;" tipo="primario2" @click="eliminarUsuario()">{{ contador > 0 ? contador : '' }} Eliminar</Boton>
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cancelar</Boton>
                </v-card-actions>
            </template>
            <template v-else>
                <v-card-text style="padding-bottom: 20px;">
                    El usuario fue eliminado exitosamente
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                </v-card-actions>
            </template>

        </v-card>
    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>