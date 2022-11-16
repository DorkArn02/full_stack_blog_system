import { Avatar, Button, Flex, HStack, Spacer, Text } from '@chakra-ui/react'
import axios from 'axios'
import moment from 'moment/moment'
import React from 'react'
import { useState } from 'react'
import { useEffect } from 'react'
import authServices from '../services/auth-services'
import commentService from '../services/comment-service'

export const CommentBox = ({ data }) => {

    const callDelete = () => {
        commentService.deleteCommentById(data.comment_id)
    }

    const [username, setUsername] = useState()

    useEffect(() => {
        const getData = async () => {
            await axios.get(`http://localhost:8080/api/users/${data.user_id}`).then(resp => {
                setUsername(resp.data.username)
            })
        }
        getData()
    })

    return (
        <Flex mb={2} p={4} flexDir={"column"} border="1px dotted white">
            <HStack>
                <Avatar src={`http://localhost:8080/api/users/profile/${data.user_id}`} />
                <Spacer />
                <Text>{moment().to(data.createdAt)}</Text>
            </HStack>
            <Text fontWeight={"bold"}>{username ? username : ""}</Text>
            <Text>{data.content}</Text>
            {(authServices.getCurrentUser() != null && authServices.getCurrentUser().user_id === data.user_id) ? <Flex>
                <Spacer />
                <Button onClick={callDelete} variant={"outline"} colorScheme={"red"}>Delete my comment</Button>
            </Flex> : ""}
        </Flex>
    )
}
