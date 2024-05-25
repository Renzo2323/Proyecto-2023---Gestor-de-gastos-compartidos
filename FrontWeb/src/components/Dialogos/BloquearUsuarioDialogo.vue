<script>
import Boton from '../Botones/Boton.vue'
import axios from 'axios';

export default {
    name: 'bloquearUsuarioDialogo',
    components: {
        Boton
    },
    data() {
        return {
            dialog: false,
            usuario: {},
            errorPostSubmit: "",
            terminado: false,
            estadoInicial: '',
            estadoFinal: ''
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            if(valor){
                this.terminado = false
            }
            this.dialog = valor
        },
        setearUsuario(usuario) {
            this.usuario = usuario
            if(usuario.bloqueado){
                this.estadoInicial = 'bloqueado'
                this.estadoFinal = 'desbloqueado'
            }else{
                this.estadoInicial = 'desbloqueado'
                this.estadoFinal = 'bloqueado'
            }
        },
        async bloquearUsuario() {//Esta funcion puede bloquear y desbloquear
            let url
            if(this.estadoInicial == 'bloqueado'){
                url = import.meta.env.VITE_API + "/api/usuarios/adm/activarUsuario/" + this.usuario.id
            }else{
                url = import.meta.env.VITE_API + "/api/usuarios/adm/desactivarUsuario/" + this.usuario.id
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
                    Seguro/a que desea <span v-if="estadoInicial == 'desbloqueado'">bloquear</span><span v-else>desbloquear</span> a <span style="color: var(--color-primario);">{{ usuario.nombre }}</span>?
                    <p id="errorControl">{{ errorPostSubmit }}</p>
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton style="margin-right: 10px;" tipo="primario2" @click="bloquearUsuario()"><span v-if="estadoInicial == 'desbloqueado'">Bloquear</span><span v-else>Desbloquear</span></Boton>
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cancelar</Boton>
                </v-card-actions>
            </template>
            <template v-else>
                <v-card-text style="padding-bottom: 20px;">
                    El usuario fue <span v-if="estadoInicial == 'desbloqueado'">bloqueado</span><span v-else>desbloqueado</span> exitosamente
                </v-card-text>
                <v-card-actions style="padding-left: 22px;">
                    <Boton tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                </v-card-actions>
            </template>

        </v-card>
    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>