org.springframework.cloud.contract.spec.Contract.make {
    description 'should return foo 333'
    request {
        method 'GET'
        url '/foos/333'
        headers {
            header('Authorization', 'Bearer bar-token')
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
    {
        "id": 333,
        "name": "foo-value-333"
    }
""")
        headers {
            header('Content-Type': 'application/json')
        }
    }
}
