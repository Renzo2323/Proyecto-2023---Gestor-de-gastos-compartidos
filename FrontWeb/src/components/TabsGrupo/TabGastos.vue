<script>
import axios from 'axios';
import EliminarGastoDialogo from '../Dialogos/EliminarGastoDialogo.vue';

export default {
    name: 'tabGastos',
    components: {
        EliminarGastoDialogo
    },
    data() {
        return {
            show: null,
        }
    },
    methods: {
        eliminar(gastoId, gastoNombre) {
            
            this.$refs.eliminarGasto.setDatos(gastoId, gastoNombre)
            this.$refs.eliminarGasto.toggleDialog(true)
            
        },
        calcularDeuda(participante, gastoId) {
            let monto = participante.cuotaParticipacion
            let avatarId = participante.avatar
            let gasto = this.gastos.find(g => g.gastoId == gastoId)
            
            let pagador = gasto.pagadores.find(p => p.avatar == avatarId)
            if (pagador != undefined) {
                
                return monto - pagador.pago
            }
            return monto
        },
        getPagadoresNoParticipantes(gasto) {
            let pagadoresNoParticipantes = []
            gasto.pagadores.map(p => {
                if (gasto.participantes.find(par => par.avatar == p.avatar) == undefined) {
                    pagadoresNoParticipantes.push(p)
                }
            })
            
            
            return pagadoresNoParticipantes
        }
    },
    props: {
        todosParticipantes: Array,
        gastos: Array
    }
}
</script>


<template>
    <EliminarGastoDialogo ref="eliminarGasto" @refetch="$emit('refetch')"/>
    <div style="margin-top: 15px;">
        <div v-if="gastos.length == 0" style="text-align:center; margin-top: 80px">
            No hay gastos registrados en el grupo
        </div>
        <template v-for="(gasto, i) in gastos">
            <v-card class="mx-auto"
                style="width: 90%; margin-bottom: 10px; border-width: 1px; border-style:solid; border-color:rgb(202, 202, 202)">
                <div style="display: flex;justify-content: space-between; margin-bottom: 13px">
                    <div class="d-flex flex-no-wrap">
                        <img v-if="gasto.imagen == null || gasto.imagen == ''" src='/src/assets/images/Gasto.png'
                            style="width: 140px;" class="squared" />
                        <!-- Con imagen -->
                        <img v-else :src="`data:image/png;base64,${gasto.imagen}`" class="squared">
                        <div>
                            <v-chip size="small"
                                style="font-size: 11px; margin-left: 8px; margin-top: 11px; background-color: var(--color-primario); color: white;">
                                <template v-slot:prepend>
                                    <v-icon style="margin-right: 5px;"> mdi-{{ gasto.categoria.icono }} </v-icon>
                                </template>
                                {{ gasto.categoria.nombre }}
                            </v-chip>
                            <v-card-title style="font-size: 23px; margin-top: 3px;">
                                {{ gasto.descripcion }}
                            </v-card-title>
                            <div style="display: flex; gap: 6px; align-items: center; margin-left: 11px;">
                                <!-- <p style="margin-right: 8px;">Con: </p> -->
                                Pagado por
                                <template v-for="(pagador, k) in gasto.pagadores">
                                    <v-chip
                                        @click="$emit('mostrarInfo', todosParticipantes.find(p => p.id == pagador.avatar))"
                                        style="font-size: 11px; margin-top: 5px;" variant="outlined"
                                        :style="todosParticipantes.find(p => p.id == pagador.avatar).estaVinculado ? 'padding-left: 4px;' : ''">
                                        <template v-slot:prepend
                                            v-if="todosParticipantes.find(p => p.id == pagador.avatar).estaVinculado">
                                            <!-- Sin imagen -->
                                            <img v-if="todosParticipantes.find(p => p.id == pagador.avatar).imagen == null || todosParticipantes.find(p => p.id == pagador.avatar).imagen == ''"
                                                src="/src/assets/svg/User2.svg"
                                                style="width: 25px;margin-top: 0px; margin-right: 8px;" />
                                            <!-- Con imagen -->
                                            <img v-else
                                                :src="`data:image/png;base64,${todosParticipantes.find(p => p.id == pagador.avatar).imagen}`"
                                                class="roundedd" style="margin-right: 8px;">
                                        </template>
                                        {{ todosParticipantes.find(p => p.id == pagador.avatar).nombre }} ${{ pagador.pago.toFixed(2)
                                        }}
                                    </v-chip>
                                </template>
                            </div>
                        </div>
                    </div>
                    <div style="display: flex; flex-direction: column; justify-content: space-between;">
                        <v-chip
                            style="font-size: 11px; margin-left: 8px; margin-right:10px; margin-top: 11px; background-color: var(--color-primario); color: white;">
                            ${{ gasto.monto.toFixed(2) }}
                        </v-chip>
                        <v-btn density="comfortable" variant="text"
                            style="align-self: flex-end; margin: 5px; font-size: 20px"
                            :icon="show == i ? 'mdi-chevron-up' : 'mdi-chevron-down'"
                            @click="show == i ? show = null : show = i"></v-btn>
                    </div>

                </div>

                <v-expand-transition>
                    <div v-show="show == i">

                        <v-divider></v-divider>
                        <div style="display:flex; margin-top: 10px; margin-bottom: 10px">
                            <div
                                style="display:flex;flex-basis: 40%; flex-direction: column; align-items: flex-start; gap: 5px; margin-left: 35px; ;">
                                <template v-for="(pagador, k) in getPagadoresNoParticipantes(gasto)">
                                    <div style="display: flex; align-items: center;">
                                        <v-chip
                                            @click="$emit('mostrarInfo', todosParticipantes.find(p => p.id == pagador.avatar))"
                                            style="font-size: 11px; margin-right: 11px;" variant="outlined"
                                            :style="todosParticipantes.find(p => p.id == pagador.avatar).estaVinculado ? 'padding-left: 4px;' : ''">
                                            <template v-slot:prepend
                                                v-if="todosParticipantes.find(p => p.id == pagador.avatar).estaVinculado">
                                                <!-- Sin imagen -->
                                                <img v-if="todosParticipantes.find(p => p.id == pagador.avatar).imagen == null || todosParticipantes.find(p => p.id == pagador.avatar).imagen == ''"
                                                    src="/src/assets/svg/User2.svg"
                                                    style="width: 25px;margin-top: 0px; margin-right: 8px;" />
                                                <!-- Con imagen -->
                                                <img v-else
                                                    :src="`data:image/png;base64,${todosParticipantes.find(p => p.id == pagador.avatar).imagen}`"
                                                    class="roundedd" style="margin-right: 8px;">
                                            </template>
                                            {{ todosParticipantes.find(p => p.id == pagador.avatar).nombre }}
                                        </v-chip>
                                        le deben
                                        <v-chip size="small"
                                            style="font-size: 11px; margin-left: 8px; background-color: var(--color-primario); color: white;">${{
                                                Math.abs(pagador.pago).toFixed(2) }}</v-chip>
                                    </div>
                                </template>
                                <template v-for="(participante, j) in gasto.participantes">
                                    <template v-if="calcularDeuda(participante, gasto.gastoId) > 0" :key="j">
                                        <div style="display: flex; align-items: center;">
                                            <v-chip
                                                @click="$emit('mostrarInfo', todosParticipantes.find(p => p.id == participante.avatar))"
                                                style="font-size: 11px; margin-right: 11px;" variant="outlined"
                                                :style="todosParticipantes.find(p => p.id == participante.avatar).estaVinculado ? 'padding-left: 4px;' : ''">
                                                <template v-slot:prepend
                                                    v-if="todosParticipantes.find(p => p.id == participante.avatar).estaVinculado">
                                                    <!-- Sin imagen -->
                                                    <img v-if="todosParticipantes.find(p => p.id == participante.avatar).imagen == null || todosParticipantes.find(p => p.id == participante.avatar).imagen == ''"
                                                        src="/src/assets/svg/User2.svg"
                                                        style="width: 25px;margin-top: 0px; margin-right: 8px;" />
                                                    <!-- Con imagen -->
                                                    <img v-else
                                                        :src="`data:image/png;base64,${todosParticipantes.find(p => p.id == participante.avatar).imagen}`"
                                                        class="roundedd" style="margin-right: 8px;">
                                                </template>
                                                {{ todosParticipantes.find(p => p.id == participante.avatar).nombre }}
                                            </v-chip>
                                            debe
                                            <v-chip size="small"
                                                style="font-size: 11px; margin-left: 8px; background-color: var(--color-primario); color: white;">${{
                                                    Math.abs(calcularDeuda(participante, gasto.gastoId)).toFixed(2) }}</v-chip>
                                        </div>
                                    </template>
                                    <template v-if="calcularDeuda(participante, gasto.gastoId) < 0" :key="j">
                                        <div style="display: flex; align-items: center;">
                                            <v-chip
                                                @click="$emit('mostrarInfo', todosParticipantes.find(p => p.id == participante.avatar))"
                                                style="font-size: 11px; margin-right: 11px;" variant="outlined"
                                                :style="todosParticipantes.find(p => p.id == participante.avatar).estaVinculado ? 'padding-left: 4px;' : ''">
                                                <template v-slot:prepend
                                                    v-if="todosParticipantes.find(p => p.id == participante.avatar).estaVinculado">
                                                    <!-- Sin imagen -->
                                                    <img v-if="todosParticipantes.find(p => p.id == participante.avatar).imagen == null || todosParticipantes.find(p => p.id == participante.avatar).imagen == ''"
                                                        src="/src/assets/svg/User2.svg"
                                                        style="width: 25px;margin-top: 0px; margin-right: 8px;" />
                                                    <!-- Con imagen -->
                                                    <img v-else
                                                        :src="`data:image/png;base64,${todosParticipantes.find(p => p.id == participante.avatar).imagen}`"
                                                        class="roundedd" style="margin-right: 8px;">
                                                </template>
                                                {{ todosParticipantes.find(p => p.id == participante.avatar).nombre }}
                                            </v-chip>
                                            le deben
                                            <v-chip size="small"
                                                style="font-size: 11px; margin-left: 8px; background-color: var(--color-primario); color: white;">${{
                                                    Math.abs(calcularDeuda(participante, gasto.gastoId)).toFixed(2) }}</v-chip>
                                        </div>
                                    </template>

                                </template>
                            </div>
                            <div
                                style="display: flex; justify-content: space-between; flex-basis: 60%; padding-left: 10px; border-left: 1px solid rgb(198, 198, 198);">
                                <div>
                                    <b>Nota:</b>
                                    <p style="white-space: pre-wrap;">{{ gasto.nota }}</p>
                                    <p v-if="gasto.nota == ''" style="color:gray">No tiene nota</p>
                                </div>
                                <div>
                                    <div style="display: flex; cursor: pointer; margin-right: 10px; margin-left: 10px;"
                                        @click="eliminar(gasto.gastoId, gasto.descripcion); show = null">
                                        <v-icon style="margin-right: 4px;">
                                            mdi-window-close
                                        </v-icon>
                                        <div style="white-space: nowrap;">
                                            Eliminar gasto
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </v-expand-transition>
            </v-card>
        </template>
    </div>
</template>

<style scoped>
img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 25px;
    width: 25px;
    outline: 1px solid rgb(111, 111, 111);

}

.squared {
    object-fit: cover;
    border-radius: 10%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(111, 111, 111);
    margin-top: 12px;
    margin-left: 12px;
}</style>