<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import SelectorIcono from '../../components/Modales/SelectorIcono.vue';
import axios from 'axios';


export default {
    name: 'formularioEditarCategoria',
    components: {
        Boton,
        Form,
        Field,
        ErrorMessage,
        SelectorIcono
    },
    data() {
        return {
            nombre: '',
            descripcion: '',
            icono: 'layers-triple',
            errorPostSubmit: '',
            procesando: false
        }
    },
    props: {
        categoria: Object
    },
    methods: {
        async onSubmit(valores) {
            this.procesando = true
            //Aqui se envian los datos
            
            
            const data = {
                "id": null,
                "nombre": valores.nombre,
                "descripcion": valores.descripcion,
                "icono": this.icono,
                "estado": this.categoria.estado
            }
            let url = import.meta.env.VITE_API + "/api/categorias/adm/edit/" + this.categoria.id
            const response = await axios.post(url, data, 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.$emit('categoriaCreada')
                    }
                    this.procesando = false

                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.procesando = false
                }.bind(this));



        },
        abrirIconos() {
            this.$refs.selectorIcono.toggleDialog(true)
        },
        validarTexto(valor) {
            if (!valor) {
                return 'Este campo no puede ser vacío';
            }
            return true;
        },
        iconoSeleccionado(icono) {
            
            this.icono = icono
        }
    },
    emits: ['cerrarDialogo', 'categoriaCreada'],
    mounted(){
        this.nombre = this.categoria.nombre
        this.descripcion = this.categoria.descripcionlarga
        this.icono = this.categoria.icono
    }
}
</script>

<template>
    <SelectorIcono ref="selectorIcono" @seleccionado="iconoSeleccionado" />
    <Form @submit="onSubmit">
        <div id="seccionControl">
            <div id="contenedorControl">
                <label>Nombre<Field class="textField" type="text" name="nombre" :rules="validarTexto" v-model="nombre">
                    </Field></label>
                <ErrorMessage id="errorControl" name="nombre" />
            </div>
            <div id="contenedorControl">
                <label>Descripcion
                    <Field v-slot="{ field }" v-model="descripcion" name="descripcion">
                        <textarea id="areatexto" v-bind="field" name="descripcion" />
                    </Field>
                </label>
            </div>
            <!-- Avisar a mobile que se usa los iconos de material para renderear los iconos -->
            <label>Icono</label>
            <div style="display: flex; align-items: center; margin-top: 5px;">
                <div @click="abrirIconos" id="botonIcono">
                    <v-icon color="black" style="font-size: 30px; padding: 16px; margin: 0; ">
                        mdi-{{ icono }}
                    </v-icon>
                </div>
                <div style="margin-left: 11px;">
                    {{ icono }}
                </div>
            </div>
            <p id="errorControl">{{ errorPostSubmit }}</p>
        </div>
        <Boton :loading="procesando" tipo="primario2" type="submit">Confirmar cambios</Boton>
        <Boton tipo="secundarioBorde" type="button" style="margin-left: 15px;" @click="$emit('cerrarDialogo')">Cancelar
        </Boton>
    </form>
</template>


<style scoped src="../../assets/css/formulario.css"/>

<style scoped>
#botonIcono {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    padding-left: 7px;
    padding-top: 7px;
    border-style: solid;
    border-color: gray;
    border-width: 2px;
    cursor: pointer;
    background-color: white;
}

#botonIcono:hover {
    background-color: rgb(229, 229, 229);
}
</style>