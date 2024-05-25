<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';

export default {
    name: 'formularioEditarGrupo',
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
            this.procesando = true
            let valorImagen
            if (!this.imagen.length == 0) {
                //tiene imagen
                valorImagen = null
            } else {
                valorImagen = null
            }
            //Aqui se envian los datos
            

            const data = {
                "nombre": valores.nombre,
                "tipoGrupo": valores.tipoGrupo.toUpperCase(),
                "imagen": this.base64String,
            }
            const url = import.meta.env.VITE_API + "/api/grupos/usf/actualizarMiGrupo/" + this.infoGrupo.id
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
                        this.$emit('grupoModificado')
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
        capitalizar(string){
            return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase()

        }
    },
    mounted() {
        this.nombre = this.infoGrupo.nombre
        this.tipoGrupo =this.infoGrupo.tipoGrupo.toLowerCase()
        this.imagenPerfil = this.infoGrupo.imagen
        this.base64String = this.infoGrupo.imagen
        this.fetchTipoGrupos()
    },
    props:{
        infoGrupo: Object
    },
    emits: ['grupoModificado']
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
            <!-- <div id="contenedorControl">
                <label>Imagen
                    <v-file-input disabled v-model="imagen" accept="image/*" variant="outlined" density="compact"
                        prepend-icon="" style="margin-top: 7px;"></v-file-input>
                </label>
            </div> -->
            <div style="margin-bottom: 10px"><b>Imagen</b></div>
            <div style="margin-right: 15px;position: relative;">
                <v-hover v-slot="{ isHovering, props }">
                    <div style="  height: 140px; width: 140px; border-radius: 13px;"
                        :style="imagenPerfil != null ? 'background-color: var(--color-primario);' : ''">
                        <!-- Sin imagen -->
                        <img v-bind="props" v-if="base64String == null"
                            src="/src/assets/images/Grupo.png" class="squared" style="width: 140px;margin-top: 0px;"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.3;' : ''" />
                        <!-- Con imagen -->
                        <img v-bind="props" v-else :src="`data:image/png;base64,${base64String}`" class="squared"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.5;' : ''">
                        <label for="file-input">
                            <div class="centeredd" v-show="isHovering" v-bind="props"
                                style="padding: 45px; border-radius: 50%; cursor: pointer;">
                                <v-icon style="font-size: 50px; color: white;"
                                    :style="imagenPerfil != null ? 'color: white;' : 'color: var(--color-primario);'">mdi-image-plus</v-icon>
                            </div>
                        </label>


                        <input ref="imageInput" id="file-input" type="file" accept="image/png, image/jpeg"
                            @change="handleFileUpload" style="display: none;">
                    </div>
                </v-hover>
            </div>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>

        <Boton :loading="procesando" tipo="primario2" type="submit">Confirmar cambios</Boton>
            <Boton @click="$emit('cerrarDialogo')" tipo="secundarioBorde" type="button" style="margin-left: 15px;">Cancelar </Boton>

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