<script>
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';

export default {
    name: 'formularioRecuperarContrasena',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage
    },
    data() {
        return {
            email: '',
            contrasena: '',
            confContrasena: '',
            errorPostSubmit: '',
            errorToken: '',
            tokenVerificado: false,
            procesando: false
        }
    },
    methods: {
        async onSubmit(valores) {
            this.procesando = true

            const data = {
                "nombre": null,
                "email": valores.email,
                "fechaAlta": null,
                "contrasena": valores.contrasena,
                "imagen": null,
            }
            //debugger
            const response = await axios.post(import.meta.env.VITE_API + "/api/usuarios/all/resetPassword", data)
                .then(function (response) {
                    //debugger
                    const redirect = String(response.data).replace("redirect:", "")
                    this.$router.push(redirect)
                    this.procesando = false
                }.bind(this))
                .catch(function (error) {
                    //debugger
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
            if (valor != this.contrasena) {
                return 'Las contraseñas no coinciden';
            }
            return true;
        }
    },
    async mounted() {
        
        const urlString = import.meta.env.VITE_API + "/api/usuarios/all/resetPassword/" + this.$route.params.token
        //debugger
        const response = await axios.get(urlString)
            .then(function (response) {
                //debugger
                if (response.data != "error"){
                    //Saca el email del response y lo guarda en 'email'
                    this.email = response.data
                    this.tokenVerificado = true

                }else{
                    this.errorToken = "Token no es valido"
                }
                
            }.bind(this))
            .catch(function (error) {
                //debugger
                this.errorToken = "No se pudo realizar esta accion"
            }.bind(this));
    }
}
</script>

<template>
    <Form @submit="onSubmit" v-if="tokenVerificado">
        <div id="seccionControl">
            <div>

            </div>
            <div id="contenedorControl">
                <label>Email<Field class="textField" type="email" name="email" v-model="email" disabled></Field></label>
            </div>
            <div id="contenedorControl">
                <label>Contraseña<Field class="textField" type="password" name="contrasena" autocomplete="new-password"
                        :rules="validarContrasena" v-model="contrasena"></Field></label>
                <ErrorMessage id="errorControl" name="contrasena" />
            </div>
            <Transition>
                <div id="contenedorControl" v-show="contrasena.length != 0">
                    <label>Contraseña<Field class="textField" type="password" name="confContrasena"
                            autocomplete="new-password" :rules="validarConfContrasena" v-model="confContrasena"></Field>
                    </label>
                    <ErrorMessage id="errorControl" name="confContrasena" />
                </div>
            </Transition>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>
        <Boton :loading="procesando" tipo="primario2" type="submit">Reestablecer contraseña</Boton>
    </form>
    <p id="errorControl" style="margin-top: 15px;">{{ errorToken }}</p>
</template>


<style scoped src="../../assets/css/formulario.css"/>