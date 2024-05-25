<script>
import Contenedor from '../components/Contenedores/Contenedor.vue';
import FormularioRecuperarContrasena from '../components/Formularios/FormularioRecuperarContrasena.vue';

export default{
    name: 'recuperarContrasena',
    components: {
        Contenedor,
        FormularioRecuperarContrasena
    },
    mounted(){
        this.$emit("terminoCargar")
    }
}
</script>

<template>
    <Contenedor>
        <template v-slot:titulo>
            Ingrese su nueva contraseña
        </template>
        <template v-slot:contenido>
            <FormularioRecuperarContrasena/>
        </template>
    </Contenedor>

</template>