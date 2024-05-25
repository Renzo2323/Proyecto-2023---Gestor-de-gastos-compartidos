<script>
import Boton from '../Botones/Boton.vue'
import axios from 'axios';

export default {
    name: 'eliminarUsuarioDialogo',
    components: {
        Boton
    },
    data() {
        return {
            dialog: false,
            usuario: {},
            errorPostSubmit: "",
            eliminado: false
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            if(valor){
                this.eliminado = false
            }
            this.dialog = valor
        },
        setearUsuario(usuario) {
            
            this.usuario = usuario
        },
        async eliminarUsuario() {
            const url = import.meta.env.VITE_API + "/api/usuarios/adm/borrarUsuario/" + this.usuario.id
            const response = await axios.delete(url, 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        
                        this.eliminado = true
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
            <template v-if="!eliminado">
                <v-card-text style="padding-bottom: 20px;">
                    Seguro/a que desea eliminar a <span style="color: var(--color-primario);">{{ usuario.nombre }}</span>?
                    <p id="errorControl">{{ errorPostSubmit }}</p>
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton style="margin-right: 10px;" tipo="primario2" @click="eliminarUsuario()">Eliminar</Boton>
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