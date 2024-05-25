<script>
import Contenedor from '../../components/Contenedores/Contenedor.vue';
import FormularioCrearGrupo from '../../components/Formularios/FormularioCrearGrupo.vue';

export default{
    name: 'iniciarSesion',
    components: {
        Contenedor,
        FormularioCrearGrupo
    },
    mounted(){
        this.$emit("terminoCargar")
    }
}
</script>

<template>
    <Contenedor>
        <template v-slot:titulo>
            Nuevo grupo
        </template>
        <template v-slot:contenido>
            <FormularioCrearGrupo/>
        </template>
    </Contenedor>

</template>