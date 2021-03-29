import {shallowMount} from '@vue/test-utils'
import Gallery from '@/components/Gallery'
import Preview from '@/components/Preview'

describe("Initialization Gallery", () =>{
    it("Expect Importer to be present", () =>{
        const wrapper = shallowMount(Gallery);
        expect(wrapper.find(".gallery-import").isVisible()).toBe(true);
    })

    it("Expect ", () =>{
        const wrapper = shallowMount(Gallery);
        expect(wrapper.findComponent(preview).isVisible()).toBe(false);
    })
})