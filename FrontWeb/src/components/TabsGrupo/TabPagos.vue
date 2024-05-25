<script>
import axios from 'axios';
import EliminarPago from '../Dialogos/EliminarPago.vue';

export default {
    name: 'tabPagos',
    components: {
        EliminarPago
    },
    data() {
        return {
            show: null,
        }
    },
    methods: {
        eliminar(pagoId) {
            
            this.$refs.eliminarPago.setDatos(pagoId)
            this.$refs.eliminarPago.toggleDialog(true)
        }
    },
    props: {
        todosParticipantes: Array,
        pagos : Array,
        moneda: String
    }
}
</script>


<template>
    <EliminarPago ref="eliminarPago" @refetch="$emit('refetch')"/>
    <div style="margin-top: 15px;">
        <div v-if="pagos.length == 0" style="text-align:center; margin-top: 80px">
            No hay pagos registrados en el grupo
        </div>
        <template v-for="(pago, i) in pagos">
            <v-card id="vcard" class="mx-auto"
                style="margin-bottom: 10px; border-width: 1px; border-style:solid; border-color:rgb(202, 202, 202)">
                <div style="display: flex; justify-content:space-between;">
                    <div id="hover" @click="$emit('mostrarInfo', todosParticipantes.find(p => p.id == pago.avatarPagador))" style="display: flex; align-items: center; cursor: pointer;">
                        <div
                            style="display:flex; flex-direction: column; align-items: center; margin-top: 15px; margin-left: 15px; margin-bottom: 15px; margin-right: 15px;">
                            <!-- Sin imagen -->
                            <img v-if="todosParticipantes.find(p => p.id == pago.avatarPagador).imagen == null || todosParticipantes.find(p => p.id == pago.avatarPagador).imagen ==''" src="/src/assets/svg/User2.svg"
                                style="width: 65px;margin-top: 0px" />
                            <!-- Con imagen -->
                            <img v-else :src="`data:image/png;base64,${todosParticipantes.find(p => p.id == pago.avatarPagador).imagen}`" class="roundedd" style="">
                            <div>
                                {{ todosParticipantes.find(p => p.id == pago.avatarPagador).nombre }}
                            </div>
                        </div>
                    </div>
                    <div style="flex: 1; display: flex; flex-direction: column; margin-top: 20px;">
                        <div style="margin-top: 11px;align-self: center; display: flex; align-items: center;">
                            ...pagó
                            <!-- Luego hacer que muestre la moneda correcta con this.moneda y la funcion que retorna el signo -->
                            <v-chip
                                style="font-size: 16px; margin-left: 8px; margin-right:10px; background-color: var(--color-primario); color: white;">
                                ${{ pago.monto.toFixed(2) }}
                            </v-chip>
                            {{ pago.metodoPago == "PAYPAL" ? "por PayPal a..." : "a..." }}

                        </div>

                        <div class="arrow">
                            <div class="line"></div>
                            <div class="point"></div>
                        </div>
                    </div>

                    <div style="display: flex;">
                        <div id="hover" @click="$emit('mostrarInfo', todosParticipantes.find(p => p.id == pago.avatarReceptor))" style="cursor: pointer; margin-left: 7px;">
                            <div 
                            style="display:flex; flex-direction: column; ;align-items: center; margin-top: 15px; margin-left: 13px; margin-bottom: 15px; margin-right: 13px;">
                                <!-- Sin imagen -->
                                <img v-if="todosParticipantes.find(p => p.id == pago.avatarReceptor).imagen == null || todosParticipantes.find(p => p.id == pago.avatarReceptor).imagen ==''" src="/src/assets/svg/User2.svg"
                                    style="width: 65px;margin-top: 0px;" />
                                <!-- Con imagen -->
                                <img v-else :src="`data:image/png;base64,${todosParticipantes.find(p => p.id == pago.avatarReceptor).imagen}`" class="roundedd" style="">
                                <div>
                                    {{ todosParticipantes.find(p => p.id == pago.avatarReceptor).nombre }}
                                </div>
                            </div>
                        </div>
                        
                        <div style="display: flex; flex: 0.001; flex-direction: column; justify-content: center;">

                            <v-btn density="comfortable" variant="text"
                                style="align-self: flex-end; margin: 0px; margin-right: 5px; font-size: 15px"
                                :icon="show == i ? 'mdi-chevron-up' : 'mdi-chevron-down'"
                                @click="show == i ? show = null : show = i"></v-btn>
                        </div>
                    </div>


                </div>

                <v-expand-transition>
                    <div v-show="show == i">
                        <v-divider></v-divider>
                        <div
                            style="display: flex; justify-content: space-between; margin-top: 10px; margin-bottom: 10px; flex-basis: 66.66%; padding-left: 10px; border-left: 1px solid rgb(198, 198, 198);">
                            <div>
                                <b>Nota:</b>
                                <p style="white-space: pre-wrap;">{{ pago.nota }}</p>
                                <p v-if="pago.nota == ''" style="color:gray">No tiene nota</p>
                            </div>
                            <div>
                                <div style="display: flex; cursor: pointer; margin-right: 10px; margin-left: 10px;"
                                    @click="eliminar(pago.id); show = null">
                                    <v-icon style="margin-right: 4px;">
                                        mdi-window-close
                                    </v-icon>
                                    <div style="white-space: nowrap;">
                                        Eliminar pago
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

#hover:hover{
    background-color: rgb(243, 243, 243);
}

#hover:active{
    background-color: rgb(236, 236, 236); 
}



@media (max-width: 1280px) {
    #vcard{
        width: 87%
    }
}

@media (min-width: 1280px) {
    #vcard{
        width: 65%
    }
}
</style>