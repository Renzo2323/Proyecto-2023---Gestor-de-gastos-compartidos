<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';

export default {
    name: 'formularioIniciarSesion',
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
            errorPostSubmit: '',
            procesando: false
        }
    },
    methods: {
        async onSubmit(valores) {
            this.procesando = true
            //debugger
            //Aqui se envian los datos
            
            
            const data = {
                "email": valores.email,
                "contrasena": valores.contrasena,
            }
            const response = await axios.post(import.meta.env.VITE_API + "/api/usuarios/all/login", data)
                .then(async function (response) {
                    
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    }else{

                        localStorage.setItem("token", response.data.token)


                        this.$emit('checkNotificaciones')

                        const response2 = await axios.get(import.meta.env.VITE_API + "/api/usuarios/all/rol", 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                            .then(function (response) {
                            //debugger
                            if(response.data.mensaje == "Usuario") localStorage.setItem("tipoUsuario", "usuario")
                            else if(response.data.mensaje == "Administrador") localStorage.setItem("tipoUsuario", "administrador")
                            else localStorage.setItem("tipoUsuario", "invitado")
                            

                            }.bind(this))
                            .catch(function (error) {
                            //debugger
                            }.bind(this));
                        
                        if(this.token != null){
                            const urltoken = '/aceptarInvitacion/' + this.token
                            this.$router.push(urltoken)
                        }else{
                            this.$router.push('/')
                        }
                        
                    }
                    this.procesando = false         
                }.bind(this))
                .catch(function (error) {
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
        },
        validarContrasena(valor) {
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            return true;
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
                <label>Email<Field class="textField" type="email" name="email" autocomplete="email" :rules="validarEmail"
                        v-model="email"></Field></label>
                <ErrorMessage id="errorControl" name="email" />
            </div>
            <div id="contenedorControl">
                <label>Contraseña<Field class="textField" type="password" name="contrasena" autocomplete="current-password"
                        :rules="validarContrasena" v-model="contrasena"></Field></label>
                <ErrorMessage id="errorControl" name="contrasena" />
            </div>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>
        
        <Boton :loading="procesando" tipo="primario2" type="submit">Iniciar sesión</Boton>
        <RouterLink to="/OlvidoContrasena" id="olvidoContrasena">Olvidé mi contraseña</RouterLink>
    </form>
</template>

<style scoped src="../../assets/css/formulario.css"/>