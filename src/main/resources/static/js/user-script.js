'use strict';
(() => {
    const API_ROUTE_GET_USER = 'http://' + window.location.host + '/user/user';
    const headerUserEl = document.querySelector('.js-user');
    const userInfEl = document.querySelector('.js-table-row');

    fetch(API_ROUTE_GET_USER)
        .then((resp) => resp.ok ? resp.json() : null)
        .then((user) => {
            if (user) {
                headerUserEl.innerHTML = `<b>${user.email}</b>
<span>with roles: </span>
<span>${user.roles.join(' ').replace(/ROLE_/g, '')}</span>`;
                userInfEl.innerHTML = `<td>${user.id}</td>
<td>${user.firstName}</td>
<td>${user.lastName}</td>
<td>${user.age}</td>
<td>${user.email}</td>
<td>${user.roles.join(' ').replace(/ROLE_/g, '')}</td>`;
            }
        })
})();