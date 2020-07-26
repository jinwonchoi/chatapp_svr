// mock
//import '@/fake-db/index.js'

import axios from 'axios'
//import axios from '@/axios.js'
import { ColumnHoverService } from 'ag-grid-community/dist/lib/rendering/columnHoverService';
import jwt from 'jsonwebtoken'
    // try {
    //         axios.post('http://localhost:8090/issuetool/auth/login', {
    //             "loginId":"agent01",
    //             "passwd": "passwd"
    //         }).then((result) => {
    //             console.log("================>"+result)
    //             // console.log(`email${email} === result email ${result.data}`)
    //             // expect(email).toEqual(result.data.userData.email)
    //             // accessToken = result.data.accessToken
    //             // localStorage.setItem('accessToken', accessToken)
    //         })
    //         .catch((err) => {
    //             console.log("err==================>")
    //             console.log(err)
    //             //fail("should have token")
    //             //done.fail(new Error('I want my test to fail'))
    //             done(err)
    //         })
    //         } catch(e) {
    //         console.log(e)
    //     }

/**
 * axios호출이 mock을 호출하여 값을 가져오는것을 확인
 */
describe("Test Auth through Axios & Mock", () => {

    let accessToken = null

        
    beforeEach(function () {
        axios.create({
            baseURL: "baseURL"
            // You can add your headers here
          })
    })

    test.only("login real", () => {
        console.log("login realdddddsfsdfs")
        try {

            axios.interceptors.request.use(config => {
                // perform a task before the request is sent
                console.log('Request was sent');
                console.log(config)
              
                return config;
              }, error => {
                // handle the error
                return Promise.reject(error);
              });

              axios.interceptors.response.use((response) => {
                // do something with the response data
                console.log('Response was received');
              console.log(response)
                return response;
              }, error => {
                // handle the response error
                return Promise.reject(error);
              });

              let data = JSON.stringify({
                "loginId":"agent01",
                "passwd": "passwd"
            })
            axios.post('http://localhost:8090/issuetool/auth/login', 
            data,{headers: {'Content-Type':'application/json;charset=UTF-8',
            "Access-Control-Allow-Origin": "*",}}).then((result) => {
                console.log("sadfasdfasd")
                // console.log(`email${email} === result email ${result.data}`)
                // expect(email).toEqual(result.data.userData.email)
                // accessToken = result.data.accessToken
                // localStorage.setItem('accessToken', accessToken)
            })
            .catch((err) => {
                console.log("err==================>")
                console.log(err)
                //fail("should have token")
                //done.fail(new Error('I want my test to fail'))
                done(err)
            })
            } catch(e) {
            console.log(e)
        }
    });

    it("login", () => {
        const email = 'admin@admin.com'
        const pwd = 'adminadmin'
        console.log('login')
        axios.post('/api/auth/login', {
            email,
            password: pwd
        }).then((result) => {
            consile.log('result')
            console.log(`email${email} === result email ${result.data.userData.email}`)
            expect(email).toEqual(result.data.userData.email)
            accessToken = result.data.accessToken
            localStorage.setItem('accessToken', accessToken)
        })
    });
/*
    test('refreshToken', (done) => {

        console.log("accessToken===>" + localStorage.getItem('accessToken'))
        axios.post('/api/auth/refresh-token', { accessToken: localStorage.getItem('accessToken') })
            .then((result) => {
                console.log(result)
                expect(result.status).toBe(200)
                done();
            })
            .catch((err) => {
                console.log("err==================>")
                console.log(err)
                //fail("should have token")
                //done.fail(new Error('I want my test to fail'))
                done(err)
            })
    })
    // test('How to manually fail tests', done => {
    //     done.fail(new Error('I want my test to fail'))
    //   })
    test('registerUser', (done) => {
        const user = {
            displayName: 'testDisplayName',
            email: 'email@gmail.com',
            password: 'passwd'
        }
        axios.post('/api/auth/register', {
            displayName: user.displayName,
            email: user.email,
            password: user.password
        })
            .then((result) => {
                console.log(result)
                expect(result.status).toBe(200)
                done();
            })
            .catch((err) => {
                console.log("err==================>")
                console.log(err)
                //fail("should have token")
                //done.fail(new Error('I want my test to fail'))
                done(err)
            })
    })
*/
});

// xdescribe("Test User Management through Axios & Mock", () => {
//     it("fetchUsers", (done) => {
//         console.log("fetchUsers")
//             axios.get('/api/user-management/users')
//               .then((response) => {
//                 //commit('SET_USERS', response.data)
//                 console.log(response.data.length)
//                 //resolve(response)
//                 expect(response.data.length).toBeGreaterThanOrEqual(50)
//                 done()
//             })
//             .catch((error) => { 
//                 done(error)
//                 //reject(error) 
//             })
//     })
//     test("fetchUser", () => {
//         console.log("fetchUser")
//     })
//     test("removeRecord", () => {
//         console.log("removeRecord")
//     })
// });

// xdescribe("Test Chat through Axios & Mock", () => {
//     it("sendChatMessage", () => {
//         console.log("sendChatMessage")
//     })
//     it("fechContacts", () => {
//         console.log("fechContacts")
//     })
//     it("fetchChatContacts", () => {
//         console.log("fetchChatContacts")
//     })
//     it("fetchChats", () => {
//         console.log("fetchChats")
//     })
//     it("markSeenAllMessages", () => {
//         console.log("markSeenAllMessages")
//     })
//     it("toggleIsPinned", () => {
//         console.log("toggleIsPinned")
//     })
// });

