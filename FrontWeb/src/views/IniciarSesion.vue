<script>
import Contenedor from '../components/Contenedores/Contenedor.vue';
import FormularioIniciarSesion from '../components/Formularios/FormularioIniciarSesion.vue';

export default{
    name: 'iniciarSesion',
    data(){
        return {
            token: null
        }
    },
    components: {
        Contenedor,
        FormularioIniciarSesion
    },
    mounted(){
        this.$emit("terminoCargar")
    },

}
</script>

<template>
    <Contenedor>
        <template v-slot:titulo>
            Iniciar sesión
        </template>
        <template v-slot:contenido>
            <FormularioIniciarSesion @checkNotificaciones="$emit('checkNotificaciones')" :token="$route.query.t"/>
        </template>
    </Contenedor>

</template>