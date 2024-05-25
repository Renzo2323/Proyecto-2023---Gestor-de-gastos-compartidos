<script>
import Boton from '../components/Botones/Boton.vue'
import { RouterLink } from 'vue-router'
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';
import EstadisticasUsuario from '../components/EstadisticasUsuario.vue';
import EliminarCuentaDialogo from '../components/Dialogos/EliminarCuentaDialogo.vue';
export default {
    name: 'perfil',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage,
        EstadisticasUsuario,
        EliminarCuentaDialogo
    },
    data() {
        return {
            imagenPerfil: '/src/assets/svg/User2.svg',
            nombre: '',
            apellido: '',
            email: '',
            nombreEditado: '',
            apellidoEditado: '',
            emailEditado: '',
            administrador: false,
            editando: false,
            errorPostSubmit: '',
            imagen: '',
            selectedFile: null,
            base64String: null,
            mostrarPerfil: true
        }
    },
    methods: {
        async onSubmit(valores) {
            //Aqui se envian los datos /actualizarDatosUsuario/{usuarioId}
            

            const data = {
                "nombre": valores.nombre,
                "apellido": valores.apellido,
                "email": valores.email,
                "imagen": this.base64String,
            }

            //const url = import.meta.env.VITE_API + "/api/v1/usuarios/actualizarDatosUsuario/" + localStorage.id
            const url = import.meta.env.VITE_API + "/api/usuarios/usr/actualizarMisDatosUsuario"
            const response = await axios.post(url, data, 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.$router.go()
                    }

                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

        },
        async fetchDatos() {
            //const url = import.meta.env.VITE_API + "/api/v1/usuarios/" + localStorage.id
            const url = import.meta.env.VITE_API + "/api/usuarios/usr/infoUsuario"
            const response = await axios.get(url, 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        if ('debitoTotal' in response.data) {
                            this.administrador = false
                        } else {
                            this.administrador = true
                        }
                        
                        if (response.data.imagen && response.data.imagen != 'null') {
                            this.imagenPerfil = response.data.imagen
                            this.base64String = this.imagenPerfil
                        } else {
                            this.imagenPerfil = '/src/assets/svg/User2.svg'
                        }
                        this.nombre = response.data.nombre
                        this.email = response.data.email
                        this.apellido = response.data.apellido
                    }

                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
                this.$emit("terminoCargar")
        },
        editar() {
            this.nombreEditado = this.nombre
            this.apellidoEditado = this.apellido
            this.emailEditado = this.email
            this.editando = true
        },
        salirEditar() {
            
            if(this.imagenPerfil != '/src/assets/svg/User2.svg'){
                this.base64String = this.imagenPerfil
            }else{
                this.base64String = null
            }
            
            this.$refs.imageInput.value = null;
            this.editando = false
        },
        validarTexto(valor) {
            if (!valor) {
                return 'Este campo no puede ser vacío';
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
        borrar(){
            this.$refs.eliminarCuentaDialogo.toggleDialog(true)
        },
        toggleLeftBar(valor){
            this.$emit('toggleLeftBar', valor)
        },
        toggleNavBar(valor){
            this.$emit('toggleNavBar', valor)
        },
        togglePerfil(valor){
            this.mostrarPerfil = valor
        }
    },

    mounted() {
        this.fetchDatos()
    },
    computed: {
        nombreCapitalizado() {
            return this.nombre.charAt(0).toUpperCase() + this.nombre.slice(1)
        }
    }

}
</script>

<template>
    <EliminarCuentaDialogo ref="eliminarCuentaDialogo"/>
    <div id="contenedor1">
        <v-card v-if="mostrarPerfil" class="textoVuetify">
            <template v-slot:prepend>
                <div style="margin-right: 15px;position: relative;text-align: center;">
                    <v-hover v-slot="{ isHovering, props }" :disabled="!editando">
                        <div style=" border-radius: 50%; height: 140px;" :style="imagenPerfil != '/src/assets/svg/User2.svg' ? 'background-color: var(--color-primario);' : ''">
                            <!-- Sin imagen -->
                            <img v-bind="props" v-if="imagenPerfil == '/src/assets/svg/User2.svg' && base64String == null" src="/src/assets/svg/User2.svg"
                                class="botonNavBar" style="width: 140px;margin-top: 0px;"
                                :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.3;' : ''" />
                            <!-- Con imagen -->
                            <img v-bind="props" v-else
                                :src="`data:image/png;base64,${base64String}`" class="roundedd"
                                :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.5;' : ''">
                            <label for="file-input">
                                <div class="centeredd" v-show="isHovering" v-bind="props"
                                    style="padding: 45px; border-radius: 50%; cursor: pointer;">
                                    <v-icon style="font-size: 50px; color: white;"
                                        :style="imagenPerfil != '/src/assets/svg/User2.svg' ? 'color: white;' : 'color: var(--color-primario);'">mdi-image-plus</v-icon>
                                </div>
                            </label>


                            <input ref="imageInput" id="file-input" type="file" accept="image/png, image/jpeg" @change="handleFileUpload" style="display: none;">
                        </div>
                    </v-hover>
                </div>

            </template>
            <template v-slot:append>
                <div style="height: 150px">
                    <template v-if="!editando">
                        <div style="display: flex; cursor: pointer;" @click="editar">
                            <v-icon style="margin-right: 4px;">
                                mdi-pencil
                            </v-icon>
                            Editar perfil
                        </div>
                        <div style="display: flex;color: red; cursor: pointer;" @click="borrar">
                            <v-icon style="margin-right: 4px;">
                                mdi-window-close
                            </v-icon>
                            Borrar perfil
                        </div>
                    </template>
                    <template v-else>
                        <Boton tipo="primario2" type="submit" form="form">
                            Confirmar
                        </Boton>
                        <Boton tipo="secundarioBorde" type="button" style="margin-left: 15px;" @click="salirEditar">Cancelar
                        </Boton>
                    </template>

                </div>
            </template>
            <template v-slot:title>
                <div style="display: flex; align-items: center; gap: 15px;">
                    <template v-if="!editando">
                        <h2 class="textoVuetifyHeader" style="margin-bottom: 20px; margin-top: 20px;">
                            {{ nombreCapitalizado }} {{ apellido }}
                        </h2>
                    </template>
                    <template v-else>
                        <Form @submit="onSubmit" id="form">
                            <div style="display: flex;gap: 10px;  flex-direction: column;">
                                <div style="display: flex; gap: 15px;">
                                    <div style="display: block">
                                        <label>Nombre<Field class="textField" type="text" name="nombre"
                                                :rules="validarTexto" v-model="nombreEditado">
                                            </Field></label>
                                        <ErrorMessage id="errorControl" name="nombre" />
                                    </div>
                                    <div style="display: block">
                                        <label>Apellido<Field class="textField" type="text" name="apellido"
                                                :rules="validarTexto" v-model="apellidoEditado">
                                            </Field></label>
                                        <ErrorMessage id="errorControl" name="apellido" />
                                    </div>
                                </div>
                                <div>
                                    <label style="">Email<Field class="textField" type="email" name="email"
                                            :rules="validarEmail" v-model="emailEditado">
                                        </Field></label>
                                    <ErrorMessage id="errorControl" name="email" />
                                </div>
                                <!-- <div>
                                    <label style="">Foto perfil
                                        <Field class="textField" type="text" name="foto">
                                            <input type="file" @change="handleFileUpload">
                                            <img v-if="imageUrl" :src="imageUrl" alt="Imagen seleccionada">
                                        </Field>
                                    </label>
                                </div> -->
                                <p id="errorControl">{{ errorPostSubmit }}</p>
                            </div>
                        </Form>
                    </template>
                    <v-chip v-if="administrador && !editando"
                        style="font-size: 14px; background-color: var(--color-primario); color: white;">
                        Administrador
                    </v-chip>
                </div>

            </template>

            <template v-slot:subtitle>
                <template v-if="!editando">
                    <p class="textoVuetify">
                        {{ email }}
                    </p>
                </template>
            </template>

            <!-- <template v-slot:text>
                aaa
            </template> -->
        </v-card>
        <EstadisticasUsuario :nombreUsuario="nombre" @toggleLeftBar="toggleLeftBar" @toggleNavBar="toggleNavBar" @togglePerfil="togglePerfil"/>
    </div>
</template>

<style scoped src="../assets/css/formulario.css"/>

<style scoped>
#contenedor1 {
    margin: auto;
    max-width: 80%;
    margin-top: 30px;
}

.textoVuetifyHeader {}

.imagenWrapper {
    width: 140px;
    height: 140px;
    position: relative;
    overflow: hidden;
    border-radius: 50%;
    border-width: 2px;
    border-color: rgb(143, 143, 143);
    border-style: solid;
}

.botonPerfil {
    display: inline;
    margin: 0 auto;
    margin-left: -25%;
    height: 100%;
    width: auto;
}

.textoVuetify {
    font-size: 1.6rem;
}

img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(111, 111, 111);

}

.centeredd {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}
</style>