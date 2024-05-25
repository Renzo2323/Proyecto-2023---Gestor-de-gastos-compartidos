<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';

import axios from 'axios';

export default {
    name: 'infoAvatar',
    components: {
        Boton,
        ContenedorCerrable,
        Form,
        Field,
        ErrorMessage,
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            creado: false,
            conInvitacion: false,
            avatar: {},
            email: '',
            invitar: false,
            emailVinculado: '',
            procesando: false
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            this.invitar = false
            if (valor) {
                this.creado = false
            }

            this.dialog = valor
        },
        setAvatar(integrante) {
            this.avatar = integrante
            if (this.avatar.estaVinculado) {
                
                this.fetchEmail()
            }

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
        async confirmarInvitacion(valores) {
            this.procesando = true
            

            const data = {
                "avatarId": this.avatar.id,
                "email": this.email,
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
                        this.creado = true
                    }
                    this.procesando = false
                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.procesando = false
                }.bind(this));
        },
        async fetchEmail() {
            const url = import.meta.env.VITE_API + "/api/usuarios/usf/getAvatarEmail/" + this.avatar.id
            const response2 = await axios.get(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    // debugger
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.emailVinculado = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        }
    },
    props: {
        grupoId: Number
    },
    mounted() {
        this.invitar = false
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
        <template v-if="!creado">
            <ContenedorCerrable style="width: 462px;" @cerrar="toggleDialog(false)">
                <template v-slot:contenido>
                    <div style="display:flex; flex-direction: column; align-items: center;">
                        <div>
                            <!-- Sin imagen -->
                            <img v-if="avatar.imagen == null || avatar.imagen == ''" src="/src/assets/svg/User2.svg"
                                style="width: 145px;margin-top: 0px; margin-right: 8px;" />
                            <!-- Con imagen -->
                            <img v-else :src="`data:image/png;base64,${avatar.imagen}`" class="roundedd"
                                style="margin-right: 8px;">
                        </div>
                        <h2>{{ avatar.nombre }} {{ avatar.esUsuarioActual ? '(yo)' : '' }}</h2>
                        <div v-if="avatar.estaVinculado" style="color: gray; margin-bottom:5px">
                            {{ emailVinculado }}
                        </div>
                        <div style="font-size: 20px;" :style="avatar.saldoLocal > 0 ? 'color:green' : avatar.saldoLocal == 0 ? 'color:black' : 'color:red'">
                            {{ avatar.saldoLocal < 0 ? '-' : '' }}${{ avatar.saldoLocal < 0 ? Math.abs(avatar.saldoLocal).toFixed(2) :
                                avatar.saldoLocal.toFixed(2) }} 
                        </div>
                                <div v-if="!avatar.estaVinculado"
                                    style="display: flex; flex-direction: column; align-items: center; margin-top: 15px;">
                                    <p>Este integrante no tiene un correo electrónico.</p>
                                    <p>Quiere invitar a este integrante por correo?</p>
                                    <Form @submit="confirmarInvitacion">


                                        <Transition>
                                            <div id="contenedorControl" v-if="invitar">
                                                <label>Email<Field class="textField" type="email" name="email"
                                                        autocomplete="email" :rules="validarEmail" v-model="email"
                                                        style="width: 360px;"></Field>
                                                </label>
                                                <ErrorMessage id="errorControl" name="email" />
                                            </div>
                                        </Transition>
                                        <Boton :loading="procesando" :type="invitar ? 'submit' : 'button'" @click="invitar = true"
                                            tipo="primario2" style="margin-top: 15px;">Invitar</Boton>
                                    </Form>
                                </div>
                        </div>


                        <!-- {{ avatar }} -->
                </template>
            </ContenedorCerrable>
        </template>
        <template v-else>
            <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="toggleDialog(false)">
                <template v-slot:titulo>

                </template>
                <template v-slot:contenido>
                    <div>
                        <p>Se agregó un nuevo integrante al grupo.</p>
                        <p>Cuando el usuario acepte la invitación, podrá acceder al grupo.</p>
                    </div>

                    <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">
                        Cerrar</Boton>
                </template>
            </ContenedorCerrable>
        </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>
<style scoped>
img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 145px;
    width: 145px;
    outline: 1px solid rgb(111, 111, 111);

}
</style>
