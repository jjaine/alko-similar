module.exports = {
  content: [
    "./src/**/**/*.{cljs,html,css,js}",
  ],
  theme: {
    extend: {
      padding: {
        '1/2': '50%',
        '1/4': '25%',
        full: '100%',
      },
      colors: {
        lager: '#f8a542',
        tumma_lager: '#734e29',
        pils: '#e4d30e',
        vahva_lager: '#b0811f',
        vehnäolut: '#fade5a',
        ale: '#b03a1e',
        'stout_&_porter': '#241b0e',
        erikoisuus: '#9d305f',
        ipa: '#b03a1e',
        'marjaisa_&_raikas': '#685ec4',
        'pehmeä_&_hedelmäinen': '#dcaabf',
        'mehevä_&_hilloinen': '#ce106e',
        'vivahteikas_&_kehittynyt': '#fa511f',
        'roteva_&_voimakas': '#6d3332',
        'pehmeä_&_kepeä': '#f8e848',
        'lempeä_&_makeahko': '#f07800',
        'pirteä_&_hedelmäinen': '#5bae33',
        'vivahteikas_&_ryhdikäs': '#1281a6',
        'runsas_&_paahteinen': '#a05020',
        'kepeä_&_viljainen_viskit': '#fbe37f',
        'pehmeä_&_hedelmäinen_viskit': '#31984f',
        'hedelmäinen_&_aromikas_viskit': '#e99926',
        'vivahteikas_&_ryhdikäs_viskit': '#4d6eb1',
        'runsas_&_voimakas_viskit': '#774314',
        greippinen: '#abe1fb',
        sitruksinen: '#f7ee61',
        hedelmäinen: '#f37022',
        marjaisa: '#ee3d97',
        maustetut_ja_muut: '#a25c98',
        'vs-konjakit': '#ecc300',
        'vsop-konjakit': '#ff7300',
        'xo-konjakit': '#89532f',
        muut_konjakit: '#7b858a',
        tastestyle_441: '#ebc22b',
        tastestyle_443: '#885333',
        tastestyle_445: '#6cc24a',
        tastestyle_447: '#0085ad',
        tastestyle_449: '#ea0437',
        'alko-gray': '#2a2a2a',
      },
      fontFamily: {
        'locator': ['locator-web', 'sans-serif'],
      },
      dropShadow: {
        'percentage': '0 0 1px rgba(255, 255, 255, 1)',
      },
      backgroundImage: {
        'globe': "url(data:image/svg+xml;charset%3DUS-ASCII,%3Csvg%20id%3D%22Layer_1%22%20data-name%3D%22Layer%201%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%2020.63%2020.58%22%3E%3Ctitle%3Eglobe%3C%2Ftitle%3E%3Cpath%20d%3D%22M40.31%2C29.22A10.31%2C10.31%2C0%2C1%2C1%2C30%2C19%2C10.29%2C10.29%2C0%2C0%2C1%2C40.31%2C29.22Zm-1%2C0a9.52%2C9.52%2C0%2C0%2C0-1.06-4.15h0a6.21%2C6.21%2C0%2C0%2C1-.85.85%2C6.1%2C6.1%2C0%2C0%2C1-1.43.89c-.14%2C0-.35%2C0-.44%2C0A4.84%2C4.84%2C0%2C0%2C1%2C35%2C27c-.26.14-.31.26-.31.36a.24.24%2C0%2C0%2C0%2C.17.18%2C1.21%2C1.21%2C0%2C0%2C1%2C.19.35.34.34%2C0%2C0%2C1%2C0%2C.19c.1%2C0%2C.1.07.1.07s0%2C.1.11.1a1.08%2C1.08%2C0%2C0%2C1%2C.78.63%2C1.91%2C1.91%2C0%2C0%2C1%2C0%2C.49%2C2%2C2%2C0%2C0%2C1-.28.7%2C1.63%2C1.63%2C0%2C0%2C0-.09.45V31a1.53%2C1.53%2C0%2C0%2C1-.12.67A2.39%2C2.39%2C0%2C0%2C1%2C34.71%2C33c-.05.16-.19.21-.23.35A2.37%2C2.37%2C0%2C0%2C0%2C34%2C34c-.21.23-.39.58-.65.89a2.57%2C2.57%2C0%2C0%2C1-1.38.84H31.8a2%2C2%2C0%2C0%2C1-1.52-1%2C3.14%2C3.14%2C0%2C0%2C1-.44-2.51%2C3.26%2C3.26%2C0%2C0%2C1%2C.23-.49%2C1.57%2C1.57%2C0%2C0%2C0%2C.09-.44c0-.12-.18-.31-.39-.35a1.45%2C1.45%2C0%2C0%2C0-1.27-.31%2C1.34%2C1.34%2C0%2C0%2C1-1.29-.19%2C2%2C2%2C0%2C0%2C1-.75-1.68l-.14-1c0-.21-.12-.53.05-.79a1%2C1%2C0%2C0%2C1%2C.54-.49c.39-.31.89-.54%2C1.38-.85.35-.09.7-.3%2C1.15-.4a5.8%2C5.8%2C0%2C0%2C1%2C1.17.21l.21.1a5.36%2C5.36%2C0%2C0%2C0%2C1.48%2C0c.54-.14.54-.14.54-.25s-.14-.28-.19-.45a3.19%2C3.19%2C0%2C0%2C0-.35-.63c0-.1%2C0-.1-.14-.1s0%2C0-.17%2C0a3.41%2C3.41%2C0%2C0%2C1-.44-.17%2C1.09%2C1.09%2C0%2C0%2C1-.64-1.21%2C1.82%2C1.82%2C0%2C0%2C1%2C.64-1%2C5.09%2C5.09%2C0%2C0%2C1%2C1.68-1.08A7.74%2C7.74%2C0%2C0%2C0%2C30%2C20a9.31%2C9.31%2C0%2C1%2C0%2C9.35%2C9.26Zm-4.85-8.06a.42.42%2C0%2C0%2C1-.31.09%2C7.15%2C7.15%2C0%2C0%2C0-2%2C1.2.47.47%2C0%2C0%2C0-.21.4A.3.3%2C0%2C0%2C0%2C32%2C23h.17a2.21%2C2.21%2C0%2C0%2C1%2C.63.21c.09.14.26.19.3.33a4.27%2C4.27%2C0%2C0%2C1%2C.5.85%2C3.45%2C3.45%2C0%2C0%2C1%2C.3.79c0%2C1-.89%2C1.25-1.34%2C1.29a3.8%2C3.8%2C0%2C0%2C1-2%2C0h-.19a1.08%2C1.08%2C0%2C0%2C0-.8-.15%2C1.69%2C1.69%2C0%2C0%2C0-.8.29c-.44.21-.84.44-1.28.65%2C0%2C.1-.15.14-.15.14v.31l.15.93c.09.68.21.94.35%2C1a1%2C1%2C0%2C0%2C0%2C.49%2C0%2C2.69%2C2.69%2C0%2C0%2C1%2C2%2C.3%2C1.88%2C1.88%2C0%2C0%2C1%2C.94%2C1.17A3.69%2C3.69%2C0%2C0%2C1%2C31%2C32.2a.36.36%2C0%2C0%2C0-.14.28%2C2%2C2%2C0%2C0%2C0%2C.28%2C1.73.94.94%2C0%2C0%2C0%2C.75.54%2C1.83%2C1.83%2C0%2C0%2C0%2C.65-.54%2C1.72%2C1.72%2C0%2C0%2C0%2C.54-.75%2C9.62%2C9.62%2C0%2C0%2C1%2C.54-.82c.14-.16.31-.26.35-.4a2.08%2C2.08%2C0%2C0%2C0%2C.64-.84%2C1.64%2C1.64%2C0%2C0%2C0%2C0-.44%2C1.83%2C1.83%2C0%2C0%2C1%2C0-.66c.05-.24.17-.45.22-.63a.88.88%2C0%2C0%2C0%2C.14-.45H35a1.08%2C1.08%2C0%2C0%2C1-.63-.4.55.55%2C0%2C0%2C1-.21-.4.55.55%2C0%2C0%2C0-.15-.28%2C1.54%2C1.54%2C0%2C0%2C1-.35-.56%2C1.49%2C1.49%2C0%2C0%2C1%2C.89-1.48%2C1.35%2C1.35%2C0%2C0%2C1%2C.75-.18%2C1.41%2C1.41%2C0%2C0%2C0%2C.45-.17%2C5%2C5%2C0%2C0%2C0%2C1.08-.68%2C3%2C3%2C0%2C0%2C0%2C.58-.61.61.61%2C0%2C0%2C0%2C.21-.49A8.69%2C8.69%2C0%2C0%2C0%2C34.48%2C21.16Z%22%20transform%3D%22translate%28-19.69%20-18.98%29%22%2F%3E%3C%2Fsvg%3E)",
        'grapes': "url(data:image/svg+xml;charset%3DUS-ASCII,%3Csvg%20id%3D%22Layer_1%22%20data-name%3D%22Layer%201%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%2019.94%2023.04%22%3E%3Ctitle%3Egrapes%3C%2Ftitle%3E%3Cpath%20d%3D%22M25.73%2C27.36a2.77%2C2.77%2C0%2C0%2C1-.91%2C2.14%2C3.06%2C3.06%2C0%2C0%2C1-2.15.85%2C2.71%2C2.71%2C0%2C0%2C1-2.08-.94%2C2.93%2C2.93%2C0%2C0%2C1-.87-2.05%2C2.87%2C2.87%2C0%2C0%2C1%2C.88-2.12%2C3%2C3%2C0%2C0%2C1%2C2.15-.87%2C2.83%2C2.83%2C0%2C0%2C1%2C2.12.9A2.93%2C2.93%2C0%2C0%2C1%2C25.73%2C27.36Zm3.53%2C6a2.91%2C2.91%2C0%2C0%2C1-.88%2C2.14%2C2.87%2C2.87%2C0%2C0%2C1-2.11.88%2C2.78%2C2.78%2C0%2C0%2C1-2.12-.93%2C3%2C3%2C0%2C0%2C1-.86-2.12%2C2.76%2C2.76%2C0%2C0%2C1%2C.9-2.08%2C2.93%2C2.93%2C0%2C0%2C1%2C4.17.06A2.82%2C2.82%2C0%2C0%2C1%2C29.26%2C33.41Zm3.42-6a2.92%2C2.92%2C0%2C0%2C1-2.91%2C2.92%2C3%2C3%2C0%2C0%2C1-2.13-5.08%2C2.86%2C2.86%2C0%2C0%2C1%2C4.2%2C0A3.1%2C3.1%2C0%2C0%2C1%2C32.68%2C27.43Zm0%2C12.1a2.83%2C2.83%2C0%2C0%2C1-.88%2C2.07%2C2.81%2C2.81%2C0%2C0%2C1-4.12%2C0%2C2.93%2C2.93%2C0%2C0%2C1%2C0-4.19%2C2.87%2C2.87%2C0%2C0%2C1%2C2.14-.9%2C2.52%2C2.52%2C0%2C0%2C1%2C2.05%2C1A3.25%2C3.25%2C0%2C0%2C1%2C32.68%2C39.53ZM30.77%2C23.29h-2V19.44h2Zm5.37%2C10.19a2.83%2C2.83%2C0%2C0%2C1-.88%2C2.11%2C2.88%2C2.88%2C0%2C0%2C1-2%2C.84%2C2.69%2C2.69%2C0%2C0%2C1-2.16-1%2C2.87%2C2.87%2C0%2C0%2C1-.79-2%2C2.92%2C2.92%2C0%2C0%2C1%2C.85-2.11%2C2.77%2C2.77%2C0%2C0%2C1%2C2.1-.88%2C2.65%2C2.65%2C0%2C0%2C1%2C2.11.92A3.08%2C3.08%2C0%2C0%2C1%2C36.14%2C33.48Zm3.53-5.29a3.12%2C3.12%2C0%2C0%2C1-3%2C2.2%2C2.86%2C2.86%2C0%2C0%2C1-2.16-.92%2C3%2C3%2C0%2C0%2C1-.86-2.07%2C2.72%2C2.72%2C0%2C0%2C1%2C.9-2.09%2C3%2C3%2C0%2C0%2C1%2C2.12-.83%2C3.24%2C3.24%2C0%2C0%2C1%2C3%2C2.2Z%22%20transform%3D%22translate%28-19.72%20-19.44%29%22%2F%3E%3C%2Fsvg%3E)",
        'search': "url(data:image/svg+xml;charset%3DUS-ASCII,%3C%3Fxml%20version%3D%221.0%22%20encoding%3D%22utf-8%22%3F%3E%3Csvg%20version%3D%221.1%22%20id%3D%22Layer_1%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20xmlns%3Axlink%3D%22http%3A%2F%2Fwww.w3.org%2F1999%2Fxlink%22%20x%3D%220px%22%20y%3D%220px%22%20%20viewBox%3D%220%200%2017%2017%22%20style%3D%22enable-background%3Anew%200%200%2017%2017%3B%22%20xml%3Aspace%3D%22preserve%22%3E%3Ctitle%3E%EE%98%AE%3C%2Ftitle%3E%3Cdesc%3ECreated%20with%20Sketch.%3C%2Fdesc%3E%3Cg%20id%3D%22Etusivu-02-Copy-11%22%20transform%3D%22translate%28-996.000000%2C%20-29.000000%29%22%3E%20%3Cg%20id%3D%22Group-56%22%3E%20%20%3Cpath%20fill%3D%22%23000000%22%20id%3D%22_xE62E_%22%20d%3D%22M1012.1%2C43.2c0.2%2C0.1%2C0.2%2C0.3%2C0.2%2C0.5s-0.1%2C0.4-0.2%2C0.6l-1.1%2C1.1c-0.2%2C0.2-0.4%2C0.2-0.6%2C0.2s-0.4-0.1-0.5-0.2%20%20%20l-3.8-3.8c-0.5%2C0.3-1%2C0.5-1.5%2C0.6s-1.1%2C0.2-1.7%2C0.2c-0.9%2C0-1.7-0.2-2.5-0.5s-1.4-0.8-2-1.4s-1-1.2-1.4-2s-0.5-1.6-0.5-2.5%20%20%20s0.2-1.7%2C0.5-2.5s0.8-1.4%2C1.4-2c0.6-0.6%2C1.2-1%2C2-1.4s1.6-0.5%2C2.5-0.5s1.7%2C0.2%2C2.5%2C0.5s1.4%2C0.8%2C2%2C1.4c0.6%2C0.6%2C1%2C1.2%2C1.4%2C2%20%20%20s0.5%2C1.6%2C0.5%2C2.5c0%2C0.6-0.1%2C1.1-0.2%2C1.7s-0.4%2C1-0.6%2C1.5L1012.1%2C43.2z%20M998.5%2C36.2c0%2C1.2%2C0.4%2C2.3%2C1.3%2C3.1s1.9%2C1.3%2C3.1%2C1.3%20%20%20c1.2%2C0%2C2.2-0.4%2C3.1-1.3c0.9-0.9%2C1.3-1.9%2C1.3-3.1c0-1.2-0.4-2.2-1.3-3.1s-1.9-1.3-3.1-1.3c-1.2%2C0-2.2%2C0.4-3.1%2C1.3%20%20%20C998.9%2C33.9%2C998.5%2C35%2C998.5%2C36.2z%22%2F%3E%20%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E)",
      },
    },
  },
}