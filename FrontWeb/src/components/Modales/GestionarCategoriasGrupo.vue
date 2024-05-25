<script>
import Boton from '../Botones/Boton.vue'
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue'
import { Form, Field, ErrorMessage } from 'vee-validate';
import SelectorIcono from '../../components/Modales/SelectorIcono.vue';


import axios from 'axios';

export default {
    name: 'agregarCategoriaModal',
    components: {
        Boton,
        ContenedorCerrable,
        Form,
        Field,
        ErrorMessage,
        SelectorIcono
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            creado: false,
            mostrarForm: false,
            nuevaCat: false,
            icono: 'layers-triple',
            descripcion: '',
            nombre: '',
            categoriasGrupo2: [
                {
                    "id": 1,
                    "nombre": "Categoria basica",
                    "icono": "layers-triple",
                    "descripcion": "aaaaaaaaaaaaaaaaaaaa",
                    "estado": true
                },
                {
                    "id": 2,
                    "nombre": "Categoria desactivada",
                    "icono": "layers-triple",
                    "descripcion": "eeeeeeeeeeeeeeee",
                    "estado": false
                },
                {
                    "id": 3,
                    "nombre": "Pato",
                    "icono": "duck",
                    "descripcion": "",
                    "estado": true
                },
                {
                    "id": 4,
                    "nombre": "Hueso",
                    "icono": "bone",
                    "descripcion": "",
                    "estado": true
                },
                {
                    "id": 5,
                    "nombre": "Sol",
                    "icono": "brightness-5",
                    "descripcion": "",
                    "estado": true
                },
                {
                    "id": 6,
                    "nombre": "Tijeras",
                    "icono": "content-cut",
                    "descripcion": "",
                    "estado": true
                }

            ],
            categoriasGrupo: [],
            categoriaEditando: {},
            procesando: false
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            this.mostrarForm = false
            if (valor) {
                this.dialog = true
                this.creado = false
            } else {
                this.dialog = false
            }
        },
        async menuOpcion(opcion, categoria) {
            
            switch (opcion) {
                case 'Deshabilitar':
                    let url
                    if (categoria.estado) {
                        //Deshabilitar
                        url = import.meta.env.VITE_API + "/api/categorias/usf/desactivar/" + categoria.id
                    } else {
                        //Habilitar
                        url = import.meta.env.VITE_API + "/api/categorias/usf/activar/" + categoria.id
                    }

                    const response = await axios.post(url, {},
                        {
                            headers: {
                                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                            }
                        })
                        .then(function (response) {
                            if (!response.data.estatus) {//Error
                                this.errorPostSubmit = response.data.mensaje
                            } else {
                                this.$emit('refetch')
                                this.fetchCategoriasGrupo()

                            }
                        }.bind(this))
                        .catch(function (error) {
                            this.errorPostSubmit = "No se pudo realizar esta accion"
                        }.bind(this));
                    break;
                case 'Editar':
                    this.categoriaEditando = categoria
                    this.nombre = categoria.nombre
                    this.descripcion = categoria.descripcion
                    this.icono = categoria.icono
                    this.nuevaCat = false
                    this.mostrarForm = true
                    break;
            }
        },
        estiloBloqueado(item) {
            if (item.estado) {
                return 'color:var(--color-primario)'
            }
            return ''

        },
        estiloIconoBloqueado(item) {
            if (item.estado) {
                return 'mdi-toggle-switch'
            }
            return "mdi-toggle-switch-off-outline"
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
        },
        async onSubmit(valores) {
            this.procesando = true
            //Aqui se envian los datos
            
            

            if (this.nuevaCat) {
                const data = {
                    "nombre": valores.nombre,
                    "descripcion": valores.descripcion,
                    "icono": this.icono,
                    "estado": true
                }
                const url = import.meta.env.VITE_API + "/api/categorias/usf/addGrupo/" + this.grupoId
                const response = await axios.post(url, data,
                    {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }
                    })
                    .then(function (response) {
                        // debugger
                        if (!response.data.estatus) {//Error
                            this.errorPostSubmit = response.data.mensaje
                        } else {
                            this.$emit('refetch')

                            this.creado = true
                            this.fetchCategoriasGrupo()
                        }
                        this.procesando = false
                    }.bind(this))
                    .catch(function (error) {
                        // debugger
                        this.errorPostSubmit = "No se pudo realizar esta accion"
                        this.procesando = false
                    }.bind(this));
            } else {
                const data = {
                    "nombre": valores.nombre,
                    "descripcion": valores.descripcion,
                    "icono": this.icono,
                    "estado": this.categoriaEditando.estado
                }
                let url = import.meta.env.VITE_API + "/api/categorias/usf/edit/" + this.categoriaEditando.id
                const response = await axios.post(url, data,
                    {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }
                    })
                    .then(function (response) {
                        
                        if (!response.data.estatus) {//Error
                            this.errorPostSubmit = response.data.mensaje
                        } else {
                            this.$emit('refetch')
                            this.fetchCategoriasGrupo()
                            this.mostrarForm = false
                        }
                        this.procesando = false

                    }.bind(this))
                    .catch(function (error) {
                        this.errorPostSubmit = "No se pudo realizar esta accion"
                        this.procesando = false
                    }.bind(this));
            }

        },
        async fetchCategoriasGrupo() {
            const url3 = import.meta.env.VITE_API + "/api/categorias/usf/getByGrupo/" + this.$route.params.id
            const response3 = await axios.get(url3,
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
                        this.categoriasGrupo = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        }
    },
    props: {
        grupoId: Number,
    },
    mounted() {
        this.fetchCategoriasGrupo()
    }

}
</script>

<template>
    <SelectorIcono ref="selectorIcono" @seleccionado="iconoSeleccionado" />
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
        <template v-if="!creado">
            <ContenedorCerrable style="width: 515px;" @cerrar="toggleDialog(false)">
                <template v-slot:titulo>
                    Categorías del grupo
                </template>
                <template v-slot:contenido>
                    <div style="margin-top: 15px;">
                        <div v-if="categoriasGrupo.length > 0" style="display:flex; flex-direction: column; gap: 4px;">
                            <template v-for="(categoria, i) in categoriasGrupo" :key="i">
                                <div id="itemCategoria"
                                    style="display: flex; align-items: center; padding: 10px; border-radius: 8px; justify-content: space-between;">
                                    <div style="display:flex">
                                        <v-icon>mdi-{{ categoria.icono }}</v-icon>
                                        <div style="margin-left: 8px;">
                                            {{ categoria.nombre }}
                                        </div>
                                    </div>
                                    <div>
                                        <v-icon :style="estiloBloqueado(categoria)" id="actionIcon"
                                            style="padding-right: 4px; font-size: 30px;" size="small"
                                            :icon="estiloIconoBloqueado(categoria)" class="me-2"
                                            @click="menuOpcion('Deshabilitar', categoria)" />
                                        <v-icon id="actionIcon" style="padding-right: 4px;" size="small" icon="mdi-pencil"
                                            class="me-2" @click="menuOpcion('Editar', categoria)" />
                                    </div>

                                </div>
                            </template>
                        </div>
                        <div v-else>
                            <p>No tiene categorias de grupo</p>

                        </div>
                        <Transition>
                            <Boton v-if="!mostrarForm"
                                @click="nombre = ''; descripcion = ''; icono = 'layers-triple'; mostrarForm = true; nuevaCat = true"
                                style="margin-top: 15px;" tipo="primario2" icono="mdi-plus">
                                Nueva categoria</Boton>
                        </Transition>

                        <Transition>

                            <!-- FORM DE NUEVA CATEGORIA ============================-->
                            <Form @submit="onSubmit" v-if="mostrarForm">
                                <p style="margin-top: 15px;" v-if="!nuevaCat">Editando <span
                                        style="color: var(--color-primario)">{{ categoriaEditando.nombre }}</span> </p>
                                <div id="seccionControl">
                                    <div id="contenedorControl">
                                        <label>Nombre<Field class="textField" type="text" name="nombre"
                                                :rules="validarTexto" v-model="nombre">
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
                                <Boton :loading="procesando" tipo="primario2" type="submit">{{ nuevaCat ? 'Crear categoria' : 'Confirmar cambios'}}</Boton>
                                <Boton style="margin-left: 10px;" @click="nuevaCat = false; mostrarForm = false"
                                    tipo="secundarioBorde" type="button">Cancelar</Boton>
                            </form>
                            <!-- FORM DE NUEVA CATEGORIA FIN ================================-->
                        </Transition>
                    </div>


                </template>
            </ContenedorCerrable>
        </template>
        <template v-else>
            <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="toggleDialog(false)">
                <template v-slot:titulo>

                </template>
                <template v-slot:contenido>
                    La categoria fue creada exitosamente
                    <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">
                        Cerrar</Boton>
                </template>
            </ContenedorCerrable>
        </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>

<style scoped>
#itemCategoria {
    outline: 1px solid rgb(195, 195, 195);
    margin: 1px;
    margin-right: 4px;
}

#actionIcon:hover {
    color: var(--color-primario);
}

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
}</style>