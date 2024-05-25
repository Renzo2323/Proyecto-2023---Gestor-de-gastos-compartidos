<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';

export default {
    name: 'formularioRegistro',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage
    },
    data() {
        return {
            nombre: '',
            apellido: '',
            email: '',
            fechaNacimiento: '',
            contrasena: '',
            confContrasena: '',
            errorPostSubmit: '',
            hoy: '',
            imagen: '',
            selectedFile: null,
            base64String: null,
            imagenPerfil: '/src/assets/svg/User2.svg',
            procesando: false
        }
    },
    methods: {
        async onSubmit(valores) {
            this.procesando = true
            //Aqui se envian los datos
            
            const data = {
                "nombre": valores.nombre,
                "apellido": valores.apellido,
                "email": valores.email,
                "fechaAlta": "2018-02-05T12:59:11.332",
                "fechaNacimiento": valores.fechaNacimiento,
                "contrasena": valores.contrasena,
                "imagen": this.base64String,
                "token": null,
                "debitoTotal": 0,
                "creditoTotal": 0,
                "saldoTotal": 0,
                "bloqueado": false
            }
            const response = await axios.post(import.meta.env.VITE_API + "/api/usuarios/all/agregarUsuarioFinal", data)
                .then(function (response) {
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    }else{
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
        },
        validarConfContrasena(valor) {
            if (valor != this.contrasena) {
                return 'Las contraseñas no coinciden';
            }
            return true;
        },
        validarNombre(valor) {
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            if (valor.length > 10) {
                return 'Nombre muy largo';
            }
            
            return true;
        },
        validarApellido(valor) {
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            
            return true;
        },
        validarFecha(valor){
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }



            return true

        },
        handleFileUpload(event) {
            
            const file = event.target.files[0];
            if (file) {
                this.selectedFile = file;
                const reader = new FileReader();
                reader.onload = () => {
                    this.base64String = reader.result.split(",")[1];
                };
                reader.readAsDataURL(file);
            }
        },
    },
    mounted(){
        this.hoy = new Date().toLocaleDateString()
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
                <label>Nombre<Field class="textField" type="text" name="nombre" :rules="validarNombre"
                        v-model="nombre"></Field></label>
                <ErrorMessage id="errorControl" name="nombre" />
            </div>
            <div id="contenedorControl">
                <label>Apellido<Field class="textField" type="text" name="apellido" :rules="validarApellido"
                        v-model="apellido"></Field></label>
                <ErrorMessage id="errorControl" name="apellido" />
            </div>
            <div id="contenedorControl">
                <label>Email<Field class="textField" type="email" name="email" autocomplete="email" :rules="validarEmail"
                        v-model="email"></Field></label>
                <ErrorMessage id="errorControl" name="email" />
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
            <div id="contenedorControl">
                <label>Fecha de nacimiento<Field class="textField" type="date" name="fechaNacimiento"
                        :rules="validarFecha" v-model="fechaNacimiento"></Field></label>
                <ErrorMessage id="errorControl" name="fechaNacimiento" />
            </div>
            <div style="margin-bottom: 10px"><b>Imagen</b></div>
            <div style="margin-right: 15px;position: relative;">
                <v-hover v-slot="{ isHovering, props }">
                    <div style=" border-radius: 50%; height: 140px;"
                        :style="imagenPerfil != '/src/assets/svg/User2.svg' ? 'background-color: var(--color-primario);' : ''">
                        <!-- Sin imagen -->
                        <img v-bind="props" v-if="imagenPerfil == '/src/assets/svg/User2.svg' && base64String == null"
                            src="/src/assets/svg/User2.svg" style="width: 140px;margin-top: 0px;"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.3;' : ''" />
                        <!-- Con imagen -->
                        <img v-bind="props" v-else :src="`data:image/png;base64,${base64String}`" class="roundedd"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.5;' : ''">
                        <label for="file-input">
                            <div class="centeredd" v-show="isHovering" v-bind="props"
                                style="padding: 45px; border-radius: 50%; cursor: pointer;">
                                <v-icon style="font-size: 50px; color: white;"
                                    :style="imagenPerfil != '/src/assets/svg/User2.svg' ? 'color: white;' : 'color: var(--color-primario);'">mdi-image-plus</v-icon>
                            </div>
                        </label>


                        <input ref="imageInput" id="file-input" type="file" accept="image/png, image/jpeg"
                            @change="handleFileUpload" style="display: none;">
                    </div>
                </v-hover>
            </div>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>
        <Boton :loading="procesando" tipo="primario2" type="submit">Registrarse</Boton>
        <!-- <Boton tipo="google" iconoCustom="iconoGoogle" type="button" style="margin-left: 15px;">Continuar con Google</Boton> -->
        <RouterLink to="/iniciarSesion" id="tieneCuenta"><span style="color: black;">Ya tiene cuenta?</span> Inicie sesión
        aquí</RouterLink>
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

.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(111, 111, 111);
}
</style>