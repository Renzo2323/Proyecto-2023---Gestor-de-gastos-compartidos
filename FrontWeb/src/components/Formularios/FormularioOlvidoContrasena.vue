<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';

export default {
    name: 'formularioOlvidoContrasena',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage
    },
    data() {
        return {
            email: '',
            enviado: false,
            errorPostSubmit: '',
            procesando: false
        }
    },
    emits: ["envioCorreo"],
    methods: {
        async onSubmit(valores) {
            this.procesando = true
            //Aqui se envian los datos
            
            const config = { headers: {'Content-Type': 'text/plain '} }
            const data = valores.email
            //debugger
            const response = await axios.post(import.meta.env.VITE_API + "/api/usuarios/all/requestPassword", data, config)
                .then(function (response) {
                    //debugger
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        
                        //Si da exito (email existe en el sistema y envia el correo correctamente)
                        this.enviado = true
                        this.$emit('envioCorreo')
                    }
                    this.procesando = false
                }.bind(this))
                .catch(function (error) {
                    //debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.procesando = false
                }.bind(this));

        },
        validarEmail(valor) {
            // Campo vacio
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            // Email invalido
            const regex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
            if (!regex.test(valor)) {
                return 'Email invalido';
            }

            return true;
        }
    }
}
</script>

<template>
    <p style="margin-bottom: 25px;">Ingrese su correo electrónico y le enviaremos un enlace para restablecer su contraseña.
    </p>
    <Form @submit="onSubmit">
        <div id="seccionControl">
            <div id="contenedorControl">
                <label>Email<Field class="textField" type="email" name="email" :rules="validarEmail" v-model="email">
                    </Field></label>
                <ErrorMessage id="errorControl" name="email" />
            </div>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>
        <Boton :loading="procesando" tipo="primario2" type="submit">Confirmar</Boton>
    </form>
    <Transition>
        <p v-if="enviado" style="font-size: 1.4rem; color:var(--texto-oscuro-claro);">Se ha enviado el enlace de
            recuperación, en caso de no visualizar el correo revisar la sección de spam.</p>
    </Transition>
</template>

<style scoped src="../../assets/css/formulario.css"/>