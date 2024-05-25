<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';


export default {
    name: 'formularioNuevoIntegrante',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage,
    },
    data() {
        return {
            nombre: '',
            email: '',
            invitar: false,
            errorPostSubmit: '',
            procesando: false
        }
    },
    props: {
        grupoId: Number
    },
    methods: {
        async onSubmit(valores) {
            this.procesando = true
            //Aqui se envian los datos
            
            //Crea el avatar
            const url = import.meta.env.VITE_API + "/api/grupos/usf/crearAvatarEnGrupo/" + this.grupoId + '/' + valores.nombre
            const response = await axios.post(url, {},
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(async function (response) {
                    // debugger
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        if (this.invitar) {
                            const data = {
                                "avatarId": response.data,
                                "email": valores.email,
                            }
                            const url2 = import.meta.env.VITE_API + "/api/grupos/usf/invitarAGrupo/" + this.grupoId
                            const response2 = await axios.post(url2, data,
                                {
                                    headers: {
                                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                                    }
                                })
                                .then(function (response2) {
                                    // debugger
                                    if (!response2.data.estatus) {//Error
                                        this.errorPostSubmit = response2.data.mensaje
                                    } else {
                                        this.$emit('integranteAgregadoInvitacion')
                                    }
                                    this.procesando = false

                                }.bind(this))
                                .catch(function (error) {
                                    // debugger
                                    this.errorPostSubmit = "No se pudo realizar esta accion"
                                    this.procesando = false
                                }.bind(this));
                        }else{
                            this.$emit('integranteAgregado')
                        }
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));





            

        },
        validarTexto(valor) {
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            if (valor.length > 10) {
                return 'Nombre muy largo';
            }
            return true;
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
    },
    emits: ['cerrarDialogo', 'integranteAgregado', 'integranteAgregadoInvitacion']
}
</script>

<template>
    <Form @submit="onSubmit">
        <div id="seccionControl">
            <div id="contenedorControl">
                <label>Nombre<Field class="textField" type="text" name="nombre" :rules="validarTexto" v-model="nombre">
                    </Field></label>
                <ErrorMessage id="errorControl" name="nombre" />
            </div>
            <div id="contenedorControl" style="display: flex; align-items: center; justify-content: left;">
                <label>Invitar por correo? </label>
                <v-checkbox hide-details v-model="invitar"
                    style="color: var(--color-primario); font-size: 23px; margin-left: 10px;"></v-checkbox>
            </div>
            <Transition>
                <div id="contenedorControl" v-if="invitar">
                    <label>Email<Field class="textField" type="email" name="email" autocomplete="email"
                            :rules="validarEmail" v-model="email"></Field></label>
                    <ErrorMessage id="errorControl" name="email" />
                </div>
            </Transition>


            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>
        <Boton :loading="procesando" tipo="primario2" type="submit">Confirmar</Boton>
        <!-- <Boton tipo="secundarioBorde" type="button" style="margin-left: 15px;" @click="$emit('cerrarDialogo')">Cancelar
        </Boton> -->
    </form>
</template>


<style scoped src="../../assets/css/formulario.css"/>

<style scoped></style>