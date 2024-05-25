<script>
import Contenedor from '../components/Contenedores/Contenedor.vue';
import FormularioCambioPassword from '../components/Formularios/FormularioCambioContrasena.vue';

export default{
    name: 'cambioPassword',
    components: {
        Contenedor,
        FormularioCambioPassword
    },
    mounted(){
        this.$emit("terminoCargar")
    },
}
</script>

<template>
    <Contenedor>
        <template v-slot:titulo>
            Cambio contraseña
        </template>
        <template v-slot:contenido>
            <FormularioCambioPassword :token="$route.query.t"/>
        </template>
    </Contenedor>

</template>