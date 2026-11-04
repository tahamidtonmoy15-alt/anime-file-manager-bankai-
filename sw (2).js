const C='hda-v4',SHELL=['./','index.html','manifest.webmanifest','icon-192.png','icon-512.png'];
self.addEventListener('install',e=>e.waitUntil(caches.open(C).then(c=>c.addAll(SHELL)).then(()=>self.skipWaiting())));
self.addEventListener('activate',e=>e.waitUntil(caches.keys().then(k=>Promise.all(k.filter(x=>x!==C).map(x=>caches.delete(x)))).then(()=>clients.claim())));
self.addEventListener('fetch',e=>{
 const r=e.request;
 if(r.method!=='GET')return;
 const u=new URL(r.url);
 if(u.origin!==location.origin)return;
 const isPage=r.mode==='navigate'||u.pathname.endsWith('/index.html')||u.pathname==='/';
 if(isPage){
  e.respondWith(fetch(r,{cache:'no-store'}).then(x=>{
   if(x&&x.ok){const copy=x.clone();caches.open(C).then(c=>c.put(r,copy));}
   return x;
  }).catch(()=>caches.match(r).then(x=>x||caches.match('index.html'))));
  return;
 }
 e.respondWith(caches.match(r).then(h=>h||fetch(r).then(x=>{
  if(x&&x.ok){const copy=x.clone();caches.open(C).then(c=>c.put(r,copy));}
  return x;
 }).catch(()=>caches.match('index.html'))));
});
