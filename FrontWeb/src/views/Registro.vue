<script>
import Contenedor from '../components/Contenedores/Contenedor.vue';
import FormularioRegistro from '../components/Formularios/FormularioRegistro.vue';

export default{
    name: 'registro',
    components: {
        Contenedor,
        FormularioRegistro
    },
    mounted(){
        this.$emit("terminoCargar")
    },

}
</script>

<template>
    <Contenedor>
        <template v-slot:titulo>
            Ingrese sus datos
        </template>
        <template v-slot:contenido>
            <FormularioRegistro :token="$route.query.t"/>
        </template>
    </Contenedor>

</template>