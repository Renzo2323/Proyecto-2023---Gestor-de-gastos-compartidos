<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';

export default {
    name: 'formularioCrearGrupo',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage
    },
    data() {
        return {
            nombre: '',
            tipoGrupo: 'otro',
            moneda: 'USD',
            errorPostSubmit: '',
            tiposGrupo: [],
            imagen: '',
            selectedFile: null,
            base64String: null,
            imagenPerfil: '/src/assets/images/Grupo.png',
            procesando: false
        }
    },
    methods: {
        async onSubmit(valores) {
            let valorImagen
            if (!this.imagen.length == 0) {
                //tiene imagen
                valorImagen = null
            } else {
                valorImagen = null
            }
            //Aqui se envian los datos
            
            this.procesando = true
            const data = {
                "id": null,
                "nombre": valores.nombre,
                "tipoGrupo": valores.tipoGrupo.toUpperCase(),
                "imagen": this.base64String,
                "moneda": valores.moneda
            }
            const url = import.meta.env.VITE_API + "/api/grupos/usf/agregarMiGrupoDeGastos"
            const response = await axios.put(url, data,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    //debugger
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        const id = response.data.mensaje.split(":")[1]
                        const urlGrupo = '/grupo/' + id
                        this.$router.push(urlGrupo)
                    }
                    this.procesando = false
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.procesando = false
                }.bind(this));


        },
        validarCampo(valor) {
            // Campo vacio
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            if(valor.length >= 20){
                return 'Nombre muy largo'
            }
            return true;
        },
        validarImagen(valor) {
            
        },
        async fetchTipoGrupos() {
            const response = await axios.get(import.meta.env.VITE_API + "/api/grupos/usr/tipoGrupos",
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.tiposGrupo
                        response.data.forEach(tipoGrupo => {
                            const tipoGrupoLowerCase = tipoGrupo.toLowerCase()
                            this.tiposGrupo.push(tipoGrupoLowerCase)
                        });
                    }
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
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
    mounted() {
        this.fetchTipoGrupos()
    }
}
</script>

<template>
    <Form @submit="onSubmit">
        <div id="seccionControl">
            <div id="contenedorControl">
                <label>Nombre<Field class="textField" type="name" name="nombre" v-model="nombre" :rules="validarCampo">
                    </Field></label>
                <ErrorMessage id="errorControl" name="nombre" />
            </div>
            <!-- <div id="contenedorControl">
                <label>Descripcion
                    <Field v-slot="{ field }" v-model="descripcion" name="descripcion">
                        <textarea id="areatexto" v-bind="field" name="descripcion" />
                    </Field>
                </label>
            </div> -->
            <div id="contenedorControl">
                <label>Tipo de grupo
                    <Field name="tipoGrupo" as="select" id="select" v-model="tipoGrupo" :rules="validarCampo">
                        <option v-for="(tipo, i) in tiposGrupo" :key="i" :value="tipo">{{ tipo.charAt(0).toUpperCase() +
                            tipo.slice(1) }}</option>
                    </Field>
                </label>
            </div>
            <div id="contenedorControl">
                <label>Moneda
                    <Field name="moneda" as="select" id="select" v-model="moneda" :rules="validarCampo">
                        <!-- aqui habria que buscar las monedas -->
                        <option value="USD">USD</option>
                        <option value="UYU">UYU</option>
                        <option value="UYU">ARS</option>
                        <option value="EUR">EUR</option>
                        <option value="GBP">GBP</option>
                        <option value="JPY">JPY</option>
                    </Field>
                </label>
            </div>
            <!-- <div id="contenedorControl">
                <label>Imagen
                    <v-file-input disabled v-model="imagen" accept="image/*" variant="outlined" density="compact"
                        prepend-icon="" style="margin-top: 7px;"></v-file-input>
                </label>
            </div> -->
            <div style="margin-bottom: 10px"><b>Imagen</b></div>
            <div style="margin-right: 15px;position: relative;">
                <v-hover v-slot="{ isHovering, props }">
                    <div style=" border-radius: 50%; height: 140px;"
                        :style="imagenPerfil != '/src/assets/images/Grupo.png' ? 'background-color: var(--color-primario);' : ''">
                        <!-- Sin imagen -->
                        <img v-bind="props" v-if="imagenPerfil == '/src/assets/images/Grupo.png' && base64String == null"
                            src="/src/assets/images/Grupo.png" class="squared" style="width: 140px;margin-top: 0px;"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.3;' : ''" />
                        <!-- Con imagen -->
                        <img v-bind="props" v-else :src="`data:image/png;base64,${base64String}`" class="squared"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.5;' : ''">
                        <label for="file-input">
                            <div class="centeredd" v-show="isHovering" v-bind="props"
                                style="padding: 45px; border-radius: 50%; cursor: pointer;">
                                <v-icon style="font-size: 50px; color: white;"
                                    :style="imagenPerfil != '/src/assets/images/Grupo.png' ? 'color: white;' : 'color: var(--color-primario);'">mdi-image-plus</v-icon>
                            </div>
                        </label>


                        <input ref="imageInput" id="file-input" type="file" accept="image/png, image/jpeg"
                            @change="handleFileUpload" style="display: none;">
                    </div>
                </v-hover>
            </div>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>

        <Boton :loading="procesando" tipo="primario2" type="submit">Crear grupo</Boton>
        <RouterLink to="/">
            <Boton tipo="secundarioBorde" type="button" style="margin-left: 15px;">Cancelar </Boton>
        </RouterLink>

    </form>
</template>

<style scoped src="../../assets/css/formulario.css"/>

<style scoped>
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
    left: 15%;
    transform: translate(-50%, -50%);
}

.squared {
    object-fit: cover;
    border-radius: 10%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(111, 111, 111);
}</style>