<script>
import axios from 'axios';
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue'

export default {
    name: 'resumenGrupo',
    components: {
        ContenedorCerrable
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            show: null,
            auditorias: [],
            grupoId: null,
            cargando: true,
            nombre: ''
        }
    },
    methods: {
        toggleDialog(valor) {
            
            if (valor) {
                
                this.dialog = true
            } else {
                this.dialog = false
            }
        },
        accionAIcono(accion) {
            switch (accion) {
                case 'NUEVOGRUPO':
                    return 'mdi-account-group'
                case 'NUEVOGASTO':
                    return 'mdi-receipt'
                case 'NUEVOPAGO':
                    return 'mdi-currency-usd'
                case 'CAMBIOIMAGEN':
                    return 'mdi-image-area'
                case 'CAMBIOMONEDA':
                    return 'mdi-currency-usd'
                case 'CAMBIOTIPOGRUPO':
                    return 'mdi-account-group'
                case 'NUEVACATEGORIAGRUPO':
                    return 'mdi-layers-triple'
                case 'ELIMINACATEGORIAGRUPO':
                    return 'mdi-layers-remove'
                case 'NUEVOAVATAR':
                    return 'mdi-account-plus'
                case 'NUEVAINVITACION':
                    return 'mdi-email-outline'
            }
        },
        getFecha(fecha) {
            //"2023-10-03T12:45:01.375786"
            const a = fecha.split('-')
            //"2023  10  03T12:45:01.375786"
            const b = a[2].split('T')
            //"03 12:45:01.375786"
            const c = b[1].split(':')
            //"12 45 01.375786"
            const month = a[1]
            const day = b[0]
            const year = a[0]
            const hour = c[0]
            const minute = c[1]
            return day + '/' + month + '/' + year + ' ' + hour + ':' + minute
        },
        async fetchResumen() {
            const url = import.meta.env.VITE_API + "/api/auditoria/usr/grupo/" + this.grupoId
            const response2 = await axios.get(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.auditorias = response.data.reverse()
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "Error al traer auditorias"
                }.bind(this));
                
                this.cargando = false
        },
        setGrupo(id, nombre) {
            this.cargando= true
            this.auditorias = []
            this.errorPostSubmit = ''
            this.nombre = nombre
            this.grupoId = id
            this.fetchResumen()
        }
    },
    props: {
    }
}
</script>


<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
        <ContenedorCerrable style="width: 915px; max-width: none;" @cerrar="toggleDialog(false)">
            <template v-slot:titulo>
                Resumen de <span style="color: var(--color-primario)">{{ nombre }}</span>
            </template>
            <template v-slot:contenido>
                <div style="margin-top: 20px;">
                    <div class="spinner" v-if="cargando">
                        <div class="rect1"></div>
                        <div class="rect2"></div>
                        <div class="rect3"></div>
                        <div class="rect4"></div>
                        <div class="rect5"></div>
                    </div>
                    <template v-for="(auditoria, i) in auditorias">
                        <v-card class="mx-auto"
                            style=" margin-bottom: 10px; border-width: 1px; border-style:solid; border-color:rgb(202, 202, 202)">
                            <div style="display: flex; flex-direction: column;">
                                <div
                                    style="display:flex; align-items: center; gap: 13px; padding: 20px; padding-bottom: 0px;">
                                    <v-icon>{{ accionAIcono(auditoria.accion) }}</v-icon>
                                    <div>
                                        {{ auditoria.descripcion }}
                                    </div>
                                </div>
                                <div
                                    style="font-size: 13px; color: gray; align-self: flex-end; margin-right: 8px; margin-bottom: 2px;">
                                    {{ getFecha(auditoria.fecha) }}
                                </div>
                            </div>



                        </v-card>
                    </template>
                </div>
                <p style="color:red; font-size: 14px;">{{ errorPostSubmit }}</p>

            </template>

            

        </ContenedorCerrable>
    </v-dialog>
</template>

<style scoped>
img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 65px;
    width: 65px;
    outline: 1px solid rgb(111, 111, 111);

}

.arrow {
    display: flex;
    align-items: center;
}

.line {
    width: 100%;
    background: rgb(153, 153, 153);
    height: 3px;
}

.point {
    width: 0;
    height: 0;
    border-top: 8px solid transparent;
    border-bottom: 8px solid transparent;
    border-left: 8px solid rgb(153, 153, 153);
}
</style>