<script>
import Boton from '../../components/Botones/Boton.vue';
import EliminarUsuarioDialogo from '../../components/Dialogos/EliminarUsuarioDialogo.vue'
import AgregarUsuarioModal from '../../components/Modales/AgregarUsuarioModal.vue';
import BloquearUsuarioDialogo from '../../components/Dialogos/BloquearUsuarioDialogo.vue'
import ResumenUsuario from '../../components/Modales/ResumenUsuario.vue';

import axios from 'axios';

export default {
    name: 'gestionUsuarios',
    components: {
        Boton,
        EliminarUsuarioDialogo,
        AgregarUsuarioModal,
        BloquearUsuarioDialogo,
        ResumenUsuario
    },
    data() {
        return {
            itemsPerPage: 5,
            page: 1,
            headers: [
                { title: '', align: 'end', key: 'imagen', sortable: false, width: '5%' },
                {
                    title: 'Nombre', //Nombre y apellido
                    align: 'start',
                    key: 'nombre',
                },
                { title: 'Id', align: 'end', key: 'id', align: 'left' },
                { title: 'Email', align: 'end', key: 'email', align: 'left' },
                { title: 'Rol', align: 'end', key: 'rol', align: 'left' },
                { title: 'Fecha de alta', align: 'end', key: 'fechaAlta', align: 'left' },
                { title: '', align: 'end', key: 'menu', sortable: false },

            ],
            usuarios: [],
            usuariosFiltrados: [],
            drawer: null,
            radioRol: "ambos",
            radioBlock: "ambos",
            cargando: false,
            opciones: [
                { title: 'Ver resúmen de actividad' },
                { title: 'Bloquear' },
                { title: 'Eliminar' },
            ],
            busqueda: '',
            falloFetch: false
        }
    },
    methods: {
        aplicarFiltros() {
            let resultado = this.usuarios
            if (this.radioRol != "ambos") {
                resultado = resultado.filter((usuario) => usuario.rol == this.radioRol)  
            }
            if (this.radioBlock != "ambos") {
                if(this.radioBlock == "bloqueado") resultado = resultado.filter((usuario) => usuario.bloqueado)
                else resultado = resultado.filter((usuario) => !usuario.bloqueado)
                
            }


            this.usuariosFiltrados = resultado

        },
        menuOpcion(opcion, item) {
            
            
            
            
            if (opcion == 'Eliminar') {
                this.$refs.eliminarDialogo.setearUsuario(item)
                this.$refs.eliminarDialogo.toggleDialog(true)
            } else if (opcion == 'Bloquear') {
                this.$refs.bloquearDialogo.setearUsuario(item)
                this.$refs.bloquearDialogo.toggleDialog(true)
            } else if (opcion == 'Ver resúmen de actividad') {
                this.mostrarResumen(item.id, item.nombre)
            }
        },
        async fetchUsuarios() {
            this.cargando = true
            this.usuarios = []
            this.usuariosFiltrados = []
            ////debugger
            const response = await axios.get(import.meta.env.VITE_API + "/api/usuarios/adm/", 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    this.falloFetch = false
                    response.data.forEach(usuario => {
                        let rol = "administrador"
                        if ('debitoTotal' in usuario) {
                            rol = "usuario"
                        }
                        const nombreApellido = usuario.nombre + " " + usuario.apellido
                        this.usuarios.push({
                            id: usuario.id,
                            nombre: nombreApellido,
                            email: usuario.email,
                            rol: rol,
                            fechaAlta: usuario.fechaAlta,
                            imagen: usuario.imagen,
                            bloqueado: usuario.bloqueado
                        })
                    });

                }.bind(this))
                .catch(function (error) {
                    this.falloFetch = true
                }.bind(this));
            this.aplicarFiltros()
            this.cargando = false
        },
        agregarUsuario() {
            
            this.$refs.agregarModal.toggleDialog(true)
        },
        estiloBloqueado(item){
            if (item.rol == 'administrador') {
                return 'display:none'
            }
            if(item.bloqueado){
                return 'color:red'
            }
            return ''
            
        },
        mostrarResumen(id, nombre){
            this.$refs.resumenUsuario.setUsuario(id, nombre)
            this.$refs.resumenUsuario.toggleDialog(true)
        }
    },
    async mounted() {
        this.$emit("terminoCargar")
        this.fetchUsuarios()
    },
    computed: {
        pageCount() {
            return Math.ceil(this.usuariosFiltrados.length / this.itemsPerPage)
        },
        mensajeSinUsuarios() {
            if(this.falloFetch){
                return 'No se pudieron traer los usuarios de la base de datos'
            }

            if(this.usuarios.length > 0){
                return 'Ningun usuario coincide con los parametros'
            }else{
                return 'No hay usuarios en el sistema'
            }
            
        }
    },
}
</script>



<template>
    <ResumenUsuario ref="resumenUsuario"/>
    <EliminarUsuarioDialogo ref="eliminarDialogo" @refetch="fetchUsuarios" />
    <AgregarUsuarioModal ref="agregarModal" @refetch="fetchUsuarios" />
    <BloquearUsuarioDialogo ref="bloquearDialogo" @refetch="fetchUsuarios" />
    <div id="contenedor1">
        <v-navigation-drawer location="right" v-model="drawer" temporary>
            <v-list-item>
                <template v-slot:append>
                    <v-icon @click="drawer = !drawer" icon="mdi-window-close"></v-icon>
                </template>
                <v-list-item-title class="textoVuetify">Filtros</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>
            <v-list density="compact" nav>
                <v-list-item style="margin-bottom: 0; padding-bottom: 0;">
                    <v-list-item-title class="textoVuetify">Rol</v-list-item-title>
                </v-list-item>
                <v-radio-group v-model="radioRol">
                    <v-radio value="usuario" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Usuario</div>
                        </template>
                    </v-radio>
                    <v-radio value="administrador" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Administrador</div>
                        </template>
                    </v-radio><v-radio value="ambos" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Ambos</div>
                        </template>
                    </v-radio>
                </v-radio-group>
                <v-list-item style="margin-bottom: 0; padding-bottom: 0;">
                    <v-list-item-title class="textoVuetify">Estado</v-list-item-title>
                </v-list-item>
                <v-radio-group v-model="radioBlock">
                    <v-radio value="desbloqueado" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Desbloqueado</div>
                        </template>
                    </v-radio>
                    <v-radio value="bloqueado" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Bloqueado</div>
                        </template>
                    </v-radio><v-radio value="ambos" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Ambos</div>
                        </template>
                    </v-radio>
                </v-radio-group>
                <Boton @click="aplicarFiltros" style="width: 80%; margin: 0px 25px 0 25px;" tipo="secundarioBorde">Aplicar
                    filtros</Boton>
            </v-list>
        </v-navigation-drawer>

        <div style="display: flex; justify-content: space-between; align-items: center;">
            <h2>Usuarios en el sistema</h2>
            
            <div style="display:flex; align-items: center;width: 40%;">
                <input style="max-width: 500px; width: 100%;" class="textField" name="busqueda" v-model="busqueda" />
                <v-icon style="color:rgb(103, 103, 103); margin-left:10px ;" icon="mdi-magnify"></v-icon>
            </div>
            <div style="display:flex; align-items: center;">
                <div style="flex:1; display:flex; align-items: center;">

                    <p style="color:rgb(126, 126, 126)">Usuarios por página</p>

                    <v-text-field density="compact" style="max-width: 80px;display:inline" :model-value="itemsPerPage"
                        class="pa-2" type="number" min="-1" max="15" hide-details
                        @update:model-value="itemsPerPage = parseInt($event, 10)"></v-text-field>

                </div>
                <v-icon @click.stop="drawer = !drawer" style="color:rgb(103, 103, 103);" icon="mdi-filter-outline"></v-icon>

            </div>


        </div>
        <div class="spinner" v-if="cargando">
            <div class="rect1"></div>
            <div class="rect2"></div>
            <div class="rect3"></div>
            <div class="rect4"></div>
            <div class="rect5"></div>
        </div>
        <v-data-table :no-data-text="mensajeSinUsuarios" v-else :items-per-page="itemsPerPage" :search="busqueda"
            v-model:page="page" :headers="headers" :items="usuariosFiltrados" item-value="nombre" class="elevation-1">

            <template v-slot:item.imagen="{ value }">
                <img v-if="value == null || value == ''" src='/src/assets/svg/User2.svg' class="defaultPerfil" />
                <!-- <div class="imagenWrapper" v-if="value != null">
                    <img :src="value" class="imagenPerfil" style="" />
                </div> -->
                <img v-else :src="`data:image/png;base64,${value}`" class="roundedd"/>
            </template>
            <template v-slot:item.menu="{ value, item }">
                <!-- <v-menu>
                    <template v-slot:activator="{ props }">
                        <v-icon icon="mdi-dots-vertical" v-bind="props"></v-icon>
                    </template>
                    <v-list>
                        <v-list-item v-for="(opcion, index) in opciones" :key="index" :value="index"
                            @click="menuOpcion(opcion.title, item)">
                            <v-list-item-title class="textoVuetify">{{ opcion.title }}</v-list-item-title>
                        </v-list-item>

                    </v-list>
                </v-menu> -->

                    <div style="display:inline">
                    <v-icon v-if="item.rol != 'administrador'" id="actionIcon" style="padding-right: 4px;" size="small" icon="mdi-view-list" class="me-2"
                    @click="menuOpcion('Ver resúmen de actividad', item)" >
                    </v-icon>
                    <v-tooltip activator="parent" location="bottom" >Resumen de actividad </v-tooltip>
                    </div>
                    <div style="display:inline">
                        <v-icon :style="estiloBloqueado(item)" id="actionIcon" size="small" icon="mdi-cancel" class="me-2" @click="menuOpcion('Bloquear', item)" />
                        <v-tooltip style="font-size:30px" activator="parent" location="bottom" > {{ item.bloqueado ? 'Desbloquear' : 'Bloquear' }} </v-tooltip>
                    </div>
                    <div style="display:inline">
                        <v-icon id="actionIcon" size="small" icon="mdi-window-close" class="me-2"
                        @click="menuOpcion('Eliminar', item)" />
                        <v-tooltip  activator="parent" location="bottom" >Eliminar </v-tooltip>
                    </div>



                
            </template>
            <template v-slot:bottom>
                <div style="display:flex;">
                    <div style="flex:20;">
                        <div class="text-center pt-2">
                            <v-pagination style="margin-bottom: 10px;" v-model="page" :length="pageCount"></v-pagination>

                        </div>
                    </div>
                    <div style="flex:1; display:flex; align-items: center;">
                        <div
                            style="background-color: var(--color-primario); padding: 0px; border-radius: 50%; width: 36px; height: 36px;">
                            <v-icon style="width: 100%;height: 100%" color="white" id="actionIcon" icon="mdi-plus"
                                @click="agregarUsuario" />
                        </div>
                    </div>
                </div>
            </template>
        </v-data-table>
    </div>
</template>

<style scoped>
#contenedor1 {
    margin: auto;
    max-width: 80%;
    margin-top: 30px;
}

.imagenPerfil {
    display: inline;
    margin: 0 auto;
    margin-left: -25%;
    height: 100%;
    width: auto;
}

.defaultPerfil {
    width: 33px
}

.imagenWrapper {
    width: 35px;
    height: 35px;
    position: relative;
    overflow: hidden;
    border-radius: 50%;
    border-width: 3px;
    border-color: white;
    border-style: solid;
}

.textoVuetify {
    font-size: 14px;
}

#actionIcon:hover {
    color: var(--color-primario);
}

img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 35px;
    width: 35px;
    outline: 1px solid rgb(111, 111, 111);

}



</style>

<style scoped src="../../assets/css/formulario.css"/>