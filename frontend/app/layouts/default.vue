<script>
import Header from "~/components/header.vue";
import Footer from "~/components/footer.vue";
export default {
    components: {
        Header,
        Footer,
    },
};
</script>
<template>
    <div class="flex flex-col min-h-screen">
        <Header />
        <slot />
        <Footer />
    </div>
</template>
