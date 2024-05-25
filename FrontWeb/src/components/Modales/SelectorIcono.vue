<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue'

import axios from 'axios';

export default {
    name: 'selectorIcono',
    components: {
        Boton,
        ContenedorCerrable
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            creado: false,
            allIcons: ["food", "silverware-fork-knife", "gift", "heart", "book", "car-side", "bike", "truck", "music", "filmstrip", "campfire", "bank", "bowling", "chef-hat", "elephant", "microphone-variant", "hanger", "pill", "medical-bag", "school", "paw","account", "airballoon", "airplane", "airplane-off", "alarm", "alarm-check", "album", "alert", "alert-box", "alert-circle", "alert-octagon", "ambulance", "apple", "apple-finder", "apple-ios", "apple-safari", "apps", "archive", "arrange-bring-forward", "arrange-bring-to-front", "arrange-send-backward", "arrange-send-to-back", "arrow-all", "arrow-up", "arrow-up-bold", "at", "attachment", "auto-fix", "auto-upload", "baby", "backburger", "backup-restore", "barcode", "barley", "barrel", "basket", "basket-fill", "basket-unfill", "battery", "battery-10", "battery-positive", "battery-unknown", "beach", "beaker", "beaker-outline", "beer", "bell", "bell-off", "bell-outline", "bell-ring", "bell-ring-outline", "bell-sleep", "beta", "binoculars", "bio", "biohazard", "bitbucket", "black-mesa", "blinds", "block-helper", "blogger", "bluetooth", "bluetooth-audio", "bluetooth-connect", "bluetooth-settings", "bluetooth-transfer", "blur", "blur-linear", "blur-off", "blur-radial", "bone", "book-multiple", "book-open", "book-variant", "bookmark", "bookmark-check", "bookmark-music", "bookmark-outline", "bookmark-plus", "bookmark-remove", "border-all", "border-bottom", "border-color", "border-horizontal", "border-inside", "border-left", "border-none", "border-outside", "border-right", "border-top", "border-vertical", "bowling", "box", "briefcase", "briefcase-check", "briefcase-download", "briefcase-upload", "brightness-1", "brightness-2", "brightness-3", "brightness-4", "brightness-5", "brightness-6", "brightness-7", "brightness-auto", "broom", "brush", "bug", "bulletin-board", "bullhorn", "bus", "cake", "cake-variant", "calculator", "calendar", "calendar-blank", "calendar-check", "calendar-clock", "calendar-multiple", "calendar-multiple-check", "calendar-plus", "calendar-remove", "calendar-text", "calendar-today", "camcorder", "camcorder-off", "camera", "camera-front", "camera-front-variant", "camera-iris", "camera-party-mode", "camera-rear", "camera-rear-variant", "camera-switch", "camera-timer", "candycane", "car", "car-wash", "carrot", "cart", "cart-outline", "cash", "cash-100", "cash-multiple", "cash-usd", "cast", "cast-connected", "castle", "cat", "cellphone", "cellphone-android", "cellphone-dock", "cellphone-iphone", "cellphone-link", "cellphone-link-off", "cellphone-settings", "chair-school", "chart-arc", "chart-areaspline", "chart-bar", "chart-histogram", "chart-line", "chart-pie", "check", "check-all", "checkbox-blank", "checkbox-blank-circle", "checkbox-blank-circle-outline", "checkbox-blank-outline", "checkbox-marked", "checkbox-marked-circle", "checkbox-marked-circle-outline", "checkbox-marked-outline", "checkbox-multiple-blank", "checkbox-multiple-blank-outline", "checkbox-multiple-marked", "checkbox-multiple-marked-outline", "checkerboard", "chevron-double-down", "chevron-double-left", "chevron-double-right", "chevron-double-up", "chevron-down", "chevron-left", "chevron-right", "chevron-up", "church", "city", "clipboard", "clippy", "clock", "clock-fast", "close", "cloud", "coffee", "coffee-to-go", "color-helper", "comment", "compare", "compass", "compass-outline", "console", "content-copy", "content-cut", "content-duplicate", "content-paste", "content-save", "content-save-all", "contrast", "contrast-box", "contrast-circle", "cow", "credit-card", "credit-card-multiple", "crop", "crop-free", "crop-landscape", "crop-portrait", "crop-square", "crosshairs", "crosshairs-gps", "crown", "cube", "cube-outline", "cube-unfolded", "cup", "cup-water", "currency-btc", "currency-eur", "currency-gbp", "currency-inr", "currency-rub", "currency-try", "currency-usd", "cursor-default", "cursor-default-outline", "cursor-move", "cursor-pointer", "database", "database-minus", "database-outline", "database-plus", "debug-step-into", "debug-step-out", "debug-step-over", "decimal-decrease", "decimal-increase", "delete", "delete-variant", "deskphone", "desktop-mac", "desktop-tower", "details", "deviantart", "diamond", "dice-1", "dice-2", "dice-3", "dice-4", "dice-5", "dice-6", "directions", "download", "drawing", "drawing-box", "drone", "dropbox", "drupal", "duck", "dumbbell", "earth", "earth-off", "eject", "elevation-decline", "elevation-rise", "elevator", "email", "email-open", "email-outline", "emoticon", "emoticon-cool", "emoticon-devil", "emoticon-happy", "emoticon-neutral", "emoticon-poop", "emoticon-sad", "emoticon-tongue", "engine", "engine-outline", "equal", "equal-box", "eraser", "escalator", "evernote", "exclamation", "exit-to-app", "export", "eye", "eye-off", "eyedropper", "eyedropper-variant", "facebook", "facebook-messenger", "factory", "fan", "fast-forward", "ferry", "file", "file-multiple", "film", "filter", "filter-outline", "filter-remove-outline", "filter-variant", "fire", "firefox", "fish", "flag", "flag-checkered", "flag-outline", "flag-triangle", "flag-variant", "flash", "flash-auto", "flash-off", "flashlight", "flashlight-off", "flip-to-back", "flip-to-front", "floppy", "flower", "folder", "food-apple", "food-variant", "football", "football-helmet","forum", "forward", "fridge", "fullscreen", "fullscreen-exit", "function", "gamepad", "gamepad-variant", "gas-station", "gavel", "gender-female", "gender-male", "gender-male-female", "gender-transgender", "git", "github", "glass-flute", "glass-mug", "glass-stange", "glass-tulip", "glasses", "gmail", "google", "google-chrome", "google-circles", "google-circles-communities", "google-circles-extended", "google-circles-group", "google-controller", "google-controller-off", "google-drive", "google-earth", "google-glass", "google-maps", "google-play", "google-plus", "google-hangouts", "grid", "grid-off", "group", "guitar-pick", "guitar-pick-outline", "hand-pointing-right", "hanger", "harddisk", "headphones", "headphones-box", "headphones-settings"],
        }
    },
    emits: ['seleccionado'],
    methods: {
        toggleDialog(valor) {
            if (valor) {
                this.dialog = true
            } else {
                this.dialog = false
            }
        },
        sleccionarIcono(icono) {
            this.$emit("seleccionado", icono)
            this.toggleDialog(false)
        }
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
        <ContenedorCerrable style="width: 457px;" @cerrar="toggleDialog(false)">
            <template v-slot:titulo>
                Seleccione su icono
            </template>
            <template v-slot:contenido>
                <v-sheet class="overflow-y-auto" max-height="350" style="margin-top: 14px">
                    <v-row no-gutters>
                        <v-col cols="12">
                            <!-- luego poner los filtrados solo -->
                            <div v-for="(item, i) in allIcons" :key="i" @click="sleccionarIcono(item)"
                                style="display: inline; cursor: pointer;">
                                <v-icon style="font-size: 30px; padding: 16px; margin: 0;" color="black"> mdi-{{ item }}
                                </v-icon>
                                <v-tooltip open-delay="300" activator="parent" location="top">{{item}}</v-tooltip>
                            </div>
                        </v-col>
                    </v-row>

                </v-sheet>
            </template>
        </ContenedorCerrable>
    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>