<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';

export default {
    name: 'formularioCambioContrasena',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage
    },
    data() {
        return {
            oldContrasena: '',
            nuevaContrasena: '',
            confContrasena: '',
            errorPostSubmit: '',
            procesando: false
        }
    },
    methods: {
        async onSubmit(valores) {
            this.procesando = true
            //Aqui se envian los datos
            
            const data = {
                "oldPass": valores.oldContrasena,
                "newPass": valores.nuevaContrasena,
            }

            const url = import.meta.env.VITE_API + "/api/usuarios/usr/cambiarPass"
            const response = await axios.post(url, data,
                {headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                }})
                .then(function (response) {                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    }else{
                        this.errorPostSubmit = response.data.mensaje                       
                    }
                    this.procesando = false

                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.procesando = false
                }.bind(this));

        },
        validarContrasena(valor) {
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            return true;
        },
        validarConfContrasena(valor) {
            if (valor != this.nuevaContrasena) {
                return 'Las contraseñas no coinciden';
            }
            return true;
        },
        volverMenu() {
            
            window.location.href = '/';
        }
    },
    props:{
        token : String
    }
}
</script>

<template>
    <Form @submit="onSubmit">
        <div id="seccionControl">
            <div id="contenedorControl">
                <label>Contraseña actual<Field class="textField" type="password" name="oldContrasena" autocomplete="old-password"
                        :rules="validarContrasena" v-model="oldContrasena"></Field></label>
                <ErrorMessage id="errorControl" name="contrasena" />
            </div>
            <div id="contenedorControl">
                <label>Nueva contraseña<Field class="textField" type="password" name="nuevaContrasena" autocomplete="new-password"
                        :rules="validarContrasena" v-model="nuevaContrasena"></Field></label>
                <ErrorMessage id="errorControl" name="contrasena" />
            </div>
            <Transition>
                <div id="contenedorControl" v-show="nuevaContrasena.length != 0">
                    <label>Confirmar contraseña<Field class="textField" type="password" name="confContrasena"
                            autocomplete="new-password" :rules="validarConfContrasena" v-model="confContrasena"></Field>
                        </label>
                    <ErrorMessage id="errorControl" name="confContrasena" />
                </div>
            </Transition>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>
        <Boton :loading="procesando" tipo="primario2" type="submit">Cambiar contraseña</Boton>
        <Boton tipo="secundarioBorde" style="margin-left: 15px;" @click="volverMenu()">Cerrar</Boton>
</form></template>


<style scoped src="../../assets/css/formulario.css"/>

<style scoped>

.centeredd {
    position: absolute;
    top: 50%;
    left: 15%;
    transform: translate(-50%, -50%);
}
.squared {
    object-fit: cover;
    border-radius: 10%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(111, 111, 111);
}
</style>