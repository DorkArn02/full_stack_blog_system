import { Badge, Button, Divider, Flex, Heading, Input, InputGroup, InputLeftElement, VStack } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { FaLock, FaUser } from 'react-icons/fa'
import { AiFillMail } from "react-icons/ai"
import { useNavigate } from 'react-router-dom'
import authServices from '../services/auth-services'

export const Register = () => {
    const [data, setData] = useState()
    const [error, setError] = useState()
    const navigate = useNavigate()

    useEffect(() => {
        if (localStorage.getItem('user') != null) {
            navigate('/')
        }
    }, [navigate])

    const handleChange = (e) => {
        setData({ ...data, [e.target.name]: e.target.value })
        setError("")
    }

    const onSubmit = (e) => {
        e.preventDefault()
        if (data.username.length !== 0 && data.password.length !== 0) {
            authServices.register(data).catch(err => {
                if (err.response.status === 409) {
                    setError("User with this username already exists.")
                }
            })
        }

    }
    return (
        <>
            <VStack justify={"center"} mb={10}>
                <Heading size={"lg"} textAlign={"center"}>Register page</Heading>
                <Divider width={"50%"} />
            </VStack>
            <form onSubmit={onSubmit}>
                <Flex justify={"center"}>
                    <VStack boxShadow={"lg"} p={3} width="33%">
                        <InputGroup>
                            <InputLeftElement pointerEvents='none'
                                children={<FaUser color='gray.300' />} />
                            <Input isInvalid={error} required name="username" onChange={handleChange} type="text" placeholder={"Username"}></Input>
                        </InputGroup>
                        <InputGroup>
                            <InputLeftElement pointerEvents='none'
                                children={<FaLock color='gray.300' />} />
                            <Input minLength={8} name="password" onChange={handleChange} required type="password" placeholder={"Password"}></Input>
                        </InputGroup>
                        <InputGroup>
                            <InputLeftElement pointerEvents='none'
                                children={<AiFillMail color='gray.300' />} />
                            <Input required name="email" onChange={handleChange} type="text" placeholder={"E-mail"}></Input>
                        </InputGroup>
                        <Button variant={"solid"} type="submit">Register</Button>
                        {error ? <Badge variant={"solid"} bgColor={"red.400"}>{error}</Badge> : ""}
                    </VStack>
                </Flex>
            </form>
        </>
    )
}
