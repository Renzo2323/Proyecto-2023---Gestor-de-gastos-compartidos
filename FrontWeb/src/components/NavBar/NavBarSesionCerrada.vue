<script>
import NavBarBase from './NavBarBase.vue';
import Boton from '../Botones/Boton.vue';
import { RouterLink } from 'vue-router'

export default {
    name: 'navBarSesionCerrada',
    components: {
        NavBarBase,
        Boton
    },
    data() {
        return {
            mostrarBarra: true
        }
    },
    methods:{
        toggleNavBar(valor){
            
            this.mostrarBarra = valor
        }
    }
}
</script>


<template>
    <NavBarBase v-if="mostrarBarra">
        <template v-slot:primero>
            <RouterLink :to="{path: '/registro', query: {t: $route.query.t}}" class="registroLink" style="margin: 10px;"> Registrarse</RouterLink>
        </template>
        <template v-slot:segundo>
            <RouterLink :to="{path: '/iniciarSesion', query: {t: $route.query.t}}"> <Boton tipo="secundario chico" style="margin: 10px;">Iniciar sesion</Boton></RouterLink>
        </template>
    </NavBarBase>
</template>

<style scoped>
.registroLink{
    color: white;
    text-decoration: none;
}


</style>