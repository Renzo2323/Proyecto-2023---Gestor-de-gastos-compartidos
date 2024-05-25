<script>
import { RouterLink } from 'vue-router'
import axios from 'axios'
import { DateTime } from 'luxon'

export default {
    name: 'notificaciones',
    components: {
    },
    data() {
        return {
            dialogm1: '',
            notificaciones: [
            ],
            cargando: true
        }
    },
    methods: {
        async fetchNotificaciones() {
            this.cargando = true
            const response = await axios.get(import.meta.env.VITE_API + "/api/usuarios/usr/notificacionesUsuario",
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {


                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.notificaciones = response.data.reverse()
                    }

                }.bind(this))
                .catch(function (error) {
                    // //debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
            this.cargando = false
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

            return DateTime.local(Number(year), Number(month), Number(day), Number(hour), Number(minute))
        },
        tiempoDesde(date) {
            const diff = date.diffNow(['years', 'months', 'days', 'hours', 'minutes'])
            if (diff.years < 0) {
                if(diff.years == -1){
                    return `Hace un año`
                }
                return `Hace ${diff.years.toFixed(0) * -1} años`
            } else if (diff.months < 0) {
                if(diff.months == -1){
                    return `Hace un mes`
                }
                return `Hace ${diff.months.toFixed(0) * -1} meses`
            } else if (diff.days < 0) {
                if(diff.days == -1){
                    return `Ayer`
                }
                return `Hace ${diff.days.toFixed(0) * -1} días`
            } else if (diff.hours < 0) {
                if(diff.hours == -1){
                    return `Hace una hora`
                }
                return `Hace ${diff.hours.toFixed(0) * -1} horas`
            } else if (diff.minutes <= -1.5) {
                if(diff.minutes >= -2){
                    return `Hace un minuto`
                }
                return `Hace ${diff.minutes.toFixed(0) * -1} minutos`
            } else {
                return 'Recién'
            }
        },
        async marcarLeidas(){
            const response = await axios.post(import.meta.env.VITE_API + "/api/usuarios/usf/marcarNotificacionesComoLeidas", {},
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {
                        
                    } else {
                    }

                }.bind(this))
                .catch(function (error) {
                    // //debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        }
    },
    mounted() {
        this.fetchNotificaciones()
        this.marcarLeidas()
    }
}
</script>


<template>
    <v-card max-width="600" min-height="310" class="mx-auto">
        <v-card-title class="textoVuetify" style="margin-bottom: 5px; margin-top: 5px;">Notificaciones</v-card-title>
        <v-divider></v-divider>
        <v-card-text style=" padding-top: 0;"
            :style="notificaciones.length == 0 ? 'height: 100px; width: 400px' : 'height: 300px;'">
            <v-list>
                <div class="spinner" v-if="cargando">
                    <div class="rect1"></div>
                    <div class="rect2"></div>
                    <div class="rect3"></div>
                    <div class="rect4"></div>
                    <div class="rect5"></div>
                </div>
                <div v-else v-for="(notificacion, i) in notificaciones" :key="i">
                    <v-list-item @click="$router.push('/grupo/' + notificacion.grupoId); $emit('cerrar')" color="primary" style="padding: 0; padding-right: 30px;">
                        <div style="display: flex; align-items: center; gap: 15px; ">
                            <v-icon :class="notificacion.leida ? 'leida' : 'noleida'">mdi-circle</v-icon>
                            <div>
                                <v-list-item-title v-text="notificacion.texto" class="textoVuetify"
                                    style="padding-bottom: 3px;"></v-list-item-title>
                                <v-list-item-subtitle v-text="tiempoDesde(getFecha(notificacion.fecha))"
                                    class="textoVuetify subtitulo"></v-list-item-subtitle>
                            </div>
                        </div>


                    </v-list-item>
                    <v-divider></v-divider>
                </div>
                <p v-if="notificaciones.length == 0 && !cargando"
                    style="margin-top: 28px; text-align: center; color: gray;">
                    No tiene notificaciones :(
                </p>
            </v-list>
        </v-card-text>
    </v-card>
</template>

<style scoped>
.textoVuetify {
    font-size: 16px;
}

.subtitulo {
    font-size: 12px;
    margin-top: 5px;

}

.leida{
    color: rgb(108, 108, 108); 
    font-size: 6px;
    margin-left: 2px;
    margin-right: 2px;
}

.noleida{
    color: var(--color-primario); 
    font-size: 10px;
    animation: bulletPointAnimation 1s linear infinite;
}

@keyframes bulletPointAnimation {
    0%, 100%{
        color: white
    }
    50%{
        color: var(--color-primario)
    }
}
</style>