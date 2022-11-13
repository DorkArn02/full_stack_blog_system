import { Avatar, Box, Flex, GridItem, HStack, Image, Spacer, Text, VStack } from '@chakra-ui/react'
import React from 'react'
import moment from 'moment/moment'
import { Link } from 'react-router-dom'

export const PostCard = ({ content }) => {


    return (
        <GridItem borderRadius={"20px"} boxShadow={"lg"}>
            <VStack>
                <Box>
                    <Image height={"300px"} src={`http://localhost:8080/api/posts/picture/${content.post_id}`} />
                </Box>
                <Box textAlign={"left"} width={"100%"}>
                    <HStack>
                        <Avatar size={"sm"} src={`http://localhost:8080/api/users/profile/${content.user_id}`} />
                        <Text fontWeight={"semibold"} size={"sm"}>
                            <Link to={`/posts/${content.post_id}`}>{content.title}</Link>
                        </Text>
                    </HStack>
                </Box>
                <Flex width={"100%"}>
                    <Text color={"#bbb"}>{content.author}</Text>
                    <Spacer />
                    <Text color={"#bbb"}>{moment().to(content.createdAt)}</Text>
                </Flex>
            </VStack>
        </GridItem>
    )
}
